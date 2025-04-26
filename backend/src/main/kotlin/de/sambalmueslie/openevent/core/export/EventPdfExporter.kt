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
import io.micronaut.core.io.ResourceLoader
import io.micronaut.http.server.types.files.SystemFile
import jakarta.inject.Singleton
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.tools.generic.EscapeTool
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.InputStreamReader
import java.io.StringWriter
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*
import javax.imageio.ImageIO
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource
import kotlin.jvm.optionals.getOrNull


@Singleton
class EventPdfExporter(
    private val loader: ResourceLoader,
    private val settingsService: SettingsService,
    private val timeProvider: TimeProvider
) : EventExporter {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventPdfExporter::class.java)
        private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
        private const val HEADER_PDF_FILE_SUFIX = ".pdf"
        private const val HEADER_PDF_FILE_PREFIX = "events"
    }

    private val config = loader.getResource("classpath:fop/fop.xconf").getOrNull()!!
    private val fopFactory: FopFactory = FopFactory.newInstance(File(config.toURI()))
    private val ve = VelocityEngine()

    init {
        ve.init()
    }

    override fun exportEvents(provider: () -> Sequence<EventInfo>): SystemFile? {
        val infos = provider.invoke().toList()
        return renderPdf(infos)
    }

    override fun exportEvent(info: EventInfo): SystemFile? {
        return renderPdf(listOf(info))
    }

    private fun createQrCode(event: Event): String {
        val barcodeWriter = QRCodeWriter()
        val url = settingsService.findByKey(SettingsAPI.SETTINGS_PDF_EVENT_DETAILS_URL)?.value ?: ""
        val eventUrl = "${url}${event.id}"
        val bitMatrix = barcodeWriter.encode(eventUrl, BarcodeFormat.QR_CODE, 250, 250)
        val qrCodeImage = MatrixToImageWriter.toBufferedImage(bitMatrix)
        val qrCodeByteArray = ByteArrayOutputStream()
        ImageIO.write(qrCodeImage, "png", qrCodeByteArray)
        return Base64.getEncoder().encodeToString(qrCodeByteArray.toByteArray())
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

        val qrCode = createQrCode(info.event)
        val availableSpace = getAvailableSpace(registration.registration)

        return EventPdfContent(event, location, registration, categories, qrCode, availableSpace)
    }

    private fun renderPdf(infos: List<EventInfo>): SystemFile? {
        logger.info("Start pdf rendering for ${infos.size} events")
        val content = logger.logTimeMillisWithValue("Determine content") { infos.mapNotNull { getContent(it) } }

        val escapeTool = EscapeTool()
        val properties = mapOf(
            Pair("esc", escapeTool),
            Pair("html", HtmlConverter(escapeTool)),
            Pair("content", content),
            Pair("logo", settingsService.findByKey(SettingsAPI.SETTINGS_PDF_LOGO_URL)?.value ?: ""),
            Pair("image", settingsService.findByKey(SettingsAPI.SETTINGS_PDF_IMAGE_URL)?.value ?: ""),
            Pair("date", formatter.format(LocalDateTime.now(ZoneId.of("Europe/Berlin"))))
        )
        val context = VelocityContext(properties)
        val writer = StringWriter()

        logger.logTimeMillis("Run template engine") {
            ve.evaluate(
                context,
                writer,
                "PDF Export",
                InputStreamReader(loader.getResourceAsStream("classpath:fop/event1.vm").getOrNull()!!)
            )
        }
        logger.info("Template result size ${writer.buffer.length} bytes")

        val out = ByteArrayOutputStream()
        logger.logTimeMillis("Render PDF") {
            val fop = fopFactory.newFop(MimeConstants.MIME_PDF, out)
            val factory = TransformerFactory.newInstance()
            val transformer = factory.newTransformer()
            val src = StreamSource(writer.toString().byteInputStream())
            val res = SAXResult(fop.defaultHandler)
            transformer.transform(src, res)
        }


        return logger.logTimeMillisWithValue("Write result to file with ${out.size()} bytes") {
            val file = File.createTempFile(
                HEADER_PDF_FILE_SUFIX,
                HEADER_PDF_FILE_PREFIX
            )
            file.writeBytes(out.toByteArray())
            val date = timeProvider.now().toLocalDate()
            val filename = "${date}-event-export.pdf"
            SystemFile(file).attach(filename)
        }
    }


}
