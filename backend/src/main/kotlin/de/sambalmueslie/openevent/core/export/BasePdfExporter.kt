package de.sambalmueslie.openevent.core.export

import com.google.zxing.BarcodeFormat
import com.google.zxing.client.j2se.MatrixToImageWriter
import com.google.zxing.qrcode.QRCodeWriter
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
import java.nio.file.Files
import java.nio.file.Paths
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
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        private const val HEADER_PDF_FILE_SUFIX = ".pdf"
        private const val HEADER_PDF_FILE_PREFIX = "events"
    }

    private val ve = VelocityEngine().apply {
        setProperty("resource.loader", "class")
        setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader")
        init()
    }
    private val barcodeWriter = QRCodeWriter()

    override fun exportEvents(provider: () -> Sequence<EventInfo>): SystemFile? {
        val infos = provider.invoke().toList()
        return renderPdfFile(infos)
    }

    override fun exportEvent(info: EventInfo): SystemFile? {
        return renderPdfFile(listOf(info))
    }

    private fun createQrCode(info: EventInfo): String {
        val bitMatrix = barcodeWriter.encode(getUrl(info), BarcodeFormat.QR_CODE, 250, 250)
        val qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix)
        return ByteArrayOutputStream().use { qrCodeByteArray ->
            ImageIO.write(qrCodeImage, "png", qrCodeByteArray)
            Base64.getEncoder().encodeToString(qrCodeByteArray.toByteArray())
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

        val qrCode = createQrCode(info)
        val availableSpace = getAvailableSpace(registration.registration)

        return EventPdfContent(event, location, registration, categories, qrCode, availableSpace)
    }

    private fun renderPdfFile(infos: List<EventInfo>): SystemFile? {
        logger.info("Start pdf rendering for ${infos.size} events")
        val content = logger.logTimeMillisWithValue("Determine content") { infos.mapNotNull { getContent(it) } }
        val escapeTool = EscapeTool()
        val properties = mapOf(
            Pair("esc", escapeTool),
//            Pair("html", HtmlConverter(escapeTool)),
            Pair("content", content),
            Pair("logo", convertImageToBase64(settingsService.findByKey(SettingsAPI.SETTINGS_PDF_LOGO_URL)?.value as? String ?: "")),
            Pair("image", convertImageToBase64(settingsService.findByKey(SettingsAPI.SETTINGS_PDF_IMAGE_URL)?.value as? String ?: "")),
            Pair("date", formatter.format(LocalDateTime.now(ZoneId.of("Europe/Berlin"))))
        )

        val context = VelocityContext(properties)
        val writer = StringWriter()

        logger.logTimeMillis("Run template engine") {
            val t = ve.getTemplate(template)
            t.merge(context, writer)
        }
        logger.info("Template result size ${writer.buffer.length} bytes")

        val result = writer.toString()
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

    abstract fun renderPdfContent(events: List<EventPdfContent>, content: String): ByteArrayOutputStream

    private fun convertImageToBase64(imagePath: String): String {
        return try {
            val imageBytes = Files.readAllBytes(Paths.get(imagePath))
            val extension = imagePath.substringAfterLast('.').lowercase()
            val mimeType = when (extension) {
                "jpg", "jpeg" -> "image/jpeg"
                "png" -> "image/png"
                "gif" -> "image/gif"
                "webp" -> "image/webp"
                else -> "image/jpeg"
            }
            "data:$mimeType;base64," + java.util.Base64.getEncoder().encodeToString(imageBytes)
        } catch (e: Exception) {
            println("Warning: Could not load image $imagePath - ${e.message}")
            // Return a transparent 1x1 pixel as fallback
            "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAAfFcSJAAAADUlEQVR42mP8/5+hHgAHggJ/PchI7wAAAABJRU5ErkJggg=="
        }
    }
}
