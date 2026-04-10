package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream

@Singleton
class EventNoticePdfExporter(
    settingsService: SettingsService,
    timeProvider: TimeProvider
) : BasePdfExporter("templates/event-notice-v1.vm", settingsService, timeProvider, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventNoticePdfExporter::class.java)
    }

    override fun renderPdfContent(events: List<EventPdfContent>, content: String): ByteArrayOutputStream {
        val initialPdf = ByteArrayOutputStream()

        val xhtml = Jsoup.parse(content).apply {
            outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml)
        }.html()

        ITextRenderer().apply {
            setDocumentFromString(xhtml)
            layout()
            createPDF(initialPdf)
            finishPDF()
        }

        return initialPdf
    }
}
