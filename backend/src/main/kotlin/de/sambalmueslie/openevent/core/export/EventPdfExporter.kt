package de.sambalmueslie.openevent.core.export


import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.logTimeMillis
import io.micronaut.core.io.ResourceLoader
import jakarta.inject.Singleton
import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.ByteArrayOutputStream
import java.io.File
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource
import kotlin.jvm.optionals.getOrNull


@Singleton
class EventPdfExporter(
    loader: ResourceLoader,
    settingsService: SettingsService,
    timeProvider: TimeProvider
) : BasePdfExporter("fop/event3.vm", settingsService, timeProvider, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventPdfExporter::class.java)
    }

    private val config = loader.getResource("classpath:fop/fop.xconf").getOrNull()!!
    private val fopFactory: FopFactory = FopFactory.newInstance(File(config.toURI()))

    override fun renderPdfContent(events: List<EventPdfContent>, content: String): ByteArrayOutputStream {
        val out = ByteArrayOutputStream()
        logger.logTimeMillis("Render PDF") {
            val fop = fopFactory.newFop(MimeConstants.MIME_PDF, out)
            val factory = TransformerFactory.newInstance()
            val transformer = factory.newTransformer()
            val src = StreamSource(content.byteInputStream())
            val res = SAXResult(fop.defaultHandler)
            transformer.transform(src, res)
        }
        return out
    }


}
