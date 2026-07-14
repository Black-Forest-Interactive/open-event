package de.sambalmueslie.openevent.core.export

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
import com.openhtmltopdf.outputdevice.helper.BaseRendererBuilder.FontStyle
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder
import com.openhtmltopdf.svgsupport.BatikSVGDrawer
import de.sambalmueslie.openevent.api.SettingsAPI
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.logTimeMillis
import de.sambalmueslie.openevent.logTimeMillisWithValue
import io.micronaut.http.server.types.files.SystemFile
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.tools.generic.EscapeTool
import org.slf4j.Logger
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.StringWriter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO

abstract class BasePdfExporter(
    private val template: String,
    private val settingsService: SettingsService,
    private val timeProvider: TimeProvider,
    private val logger: Logger
) : EventExporter {

    companion object {
        private val formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy, HH:mm")
        private const val HEADER_PDF_FILE_SUFIX = ".pdf"
        private const val HEADER_PDF_FILE_PREFIX = "events"
        private const val TRANSPARENT_PIXEL = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg=="
        private const val FONT_FAMILY = "Liberation Sans"
    }

    private val ve = VelocityEngine().apply {
        setProperty("resource.loader", "class")
        setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
        init()
    }
    private val barcodeWriter = QRCodeWriter()
    private val htmlConverter = HtmlConverter()

    override fun exportEvents(provider: () -> Sequence<EventInfo>): SystemFile? {
        val infos = provider.invoke().toList()
        return renderPdfFile(infos)
    }

    override fun exportEvent(info: EventInfo): SystemFile? {
        return renderPdfFile(listOf(info))
    }

    protected fun createQrCodeFromUrl(url: String): String {
        if (url.isBlank()) return ""
        return try {
            val bitMatrix = barcodeWriter.encode(url, BarcodeFormat.QR_CODE, 250, 250)
            val qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix)
            ByteArrayOutputStream().use { qrCodeByteArray ->
                ImageIO.write(qrCodeImage, "png", qrCodeByteArray)
                Base64.getEncoder().encodeToString(qrCodeByteArray.toByteArray())
            }
        } catch (e: Exception) {
            logger.warn("Could not generate QR code for url $url: ${e.message}")
            ""
        }
    }

    private fun getUrl(info: EventInfo): String {
        val share = info.share
        return if (share == null) {
            val url = settingsService.findByKey(SettingsAPI.SETTINGS_PDF_EVENT_DETAILS_URL)?.value ?: ""
            "${url}${info.event.id}"
        } else {
            share.url
        }
    }

    private fun getAvailableSpace(registration: Registration): List<Char> {
        return registration.let {
            generateSequence { 's' }.take(6).toList()
        }
    }

    private fun getContent(info: EventInfo): EventPdfContent? {
        val event: Event = info.event
        val location: Location = info.location ?: return null
        val registration: RegistrationInfo = info.registration ?: return null
        val categories: List<Category> = info.categories

        val url = getUrl(info)
        val qrCode = createQrCodeFromUrl(url)
        val availableSpace = getAvailableSpace(registration.registration)
        val description = htmlConverter.convert(event.longText)
        val shortDescription = htmlConverter.convert(event.shortText)

        return EventPdfContent(event, location, registration, categories, info.audiences, qrCode, availableSpace, description, shortDescription, url)
    }

    protected fun renderPdfFile(infos: List<EventInfo>, additionalProperties: Map<String, Any>? = null): SystemFile? {
        logger.info("Start pdf rendering for ${infos.size} events")
        val content = logger.logTimeMillisWithValue("Determine content") { infos.mapNotNull { getContent(it) } }
        val escapeTool = EscapeTool()

        val properties = mutableMapOf(
            Pair("esc", escapeTool),
            Pair("content", content),
            Pair("logo", convertImageToBase64(settingsService.findByKey(SettingsAPI.SETTINGS_PDF_LOGO_URL)?.value as? String ?: "")),
            Pair("image", convertImageToBase64(settingsService.findByKey(SettingsAPI.SETTINGS_PDF_IMAGE_URL)?.value as? String ?: "")),
            Pair("title", settingsService.findByKey(SettingsAPI.SETTINGS_TEXT_TITLE)?.value as? String ?: ""),
            Pair("date", formatter.format(LocalDateTime.now(ZoneId.of("Europe/Berlin")))),
        )

        additionalProperties?.forEach { (key, value) -> properties[key] = value }

        val context = VelocityContext(properties)
        val writer = StringWriter()

        logger.logTimeMillis("Run template engine") {
            val t = ve.getTemplate(template)
            t.merge(context, writer)
        }
        logger.info("Template result size ${writer.buffer.length} bytes")

        val result = PdfTextSanitizer.sanitize(writer.toString())
        val out = renderPdfContent(content, result)

        return logger.logTimeMillisWithValue("Write result to file with ${out.size()} bytes")
        {
            val file = File.createTempFile(
                HEADER_PDF_FILE_PREFIX,
                HEADER_PDF_FILE_SUFIX
            )
            file.writeBytes(out.toByteArray())
            val date = timeProvider.now().toLocalDate()
            val filename = "${date}-event-export.pdf"
            SystemFile(file).attach(filename)
        }
    }

    protected fun renderPdfContent(events: List<EventPdfContent>, content: String): ByteArrayOutputStream {
        val out = ByteArrayOutputStream()
        PdfRendererBuilder()
            .useSVGDrawer(BatikSVGDrawer())
            .useFont({ fontStream("LiberationSans-Regular.ttf") }, FONT_FAMILY, 400, FontStyle.NORMAL, true)
            .useFont({ fontStream("LiberationSans-Bold.ttf") }, FONT_FAMILY, 700, FontStyle.NORMAL, true)
            .useFont({ fontStream("LiberationSans-Italic.ttf") }, FONT_FAMILY, 400, FontStyle.ITALIC, true)
            .useFont({ fontStream("LiberationSans-BoldItalic.ttf") }, FONT_FAMILY, 700, FontStyle.ITALIC, true)
            .withHtmlContent(content, "about:blank")
            .toStream(out)
            .run()
        return out
    }

    private fun fontStream(name: String) = javaClass.getResourceAsStream("/fonts/$name")
        ?: throw IllegalStateException("Font /fonts/$name not found on classpath")

    private fun convertImageToBase64(imagePath: String): String {
        if (imagePath.isBlank()) return TRANSPARENT_PIXEL
        return try {
            val imageBytes = fetchImageBytes(imagePath)
            val extension = imagePath.substringAfterLast('.').substringBefore('?').lowercase()
            val mimeType = when (extension) {
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "gif" -> "image/gif"
                "webp" -> "image/webp"
                else -> "image/jpeg"
            }
            val encoded = "data:$mimeType;base64," + Base64.getEncoder().encodeToString(imageBytes)
            logger.info("Loaded image $imagePath — ${imageBytes.size} bytes, base64 length ${encoded.length}")
            encoded
        } catch (e: Exception) {
            logger.warn("Could not load image $imagePath — ${e.message}")
            TRANSPARENT_PIXEL
        }
    }

    private fun fetchImageBytes(imagePath: String): ByteArray {
        require(imagePath.startsWith("https://")) { "Only https image URLs are allowed" }
        val connection = java.net.URI(imagePath).toURL().openConnection() as java.net.HttpURLConnection
        connection.connectTimeout = 5_000
        connection.readTimeout = 15_000
        return connection.inputStream.use { it.readBytes() }
    }
}
