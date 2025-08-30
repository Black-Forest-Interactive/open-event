package de.sambalmueslie.openevent.core.export

import com.lowagie.text.pdf.PdfReader
import com.lowagie.text.pdf.PdfStamper
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream

@Singleton
class EventOpenPdfExporter(
    settingsService: SettingsService,
    timeProvider: TimeProvider
) : BasePdfExporter("templates/event-flyer-v1.vm", settingsService, timeProvider, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventOpenPdfExporter::class.java)
    }

    override fun renderPdfContent(events: List<EventPdfContent>, content: String): ByteArrayOutputStream {
        val initialPdf = ByteArrayOutputStream()
//        // Generate PDF using OpenPDF 2.4 with improved HTML support
//        val document = Document(PageSize.A4, 20f, 20f, 30f, 30f)
//        val writer = PdfWriter.getInstance(document, out)
//
//        // Enable better HTML rendering features in OpenPDF 2.4
//        writer.setViewerPreferences(PdfWriter.PageModeUseOutlines)
//        document.open()
//
//        // Use the improved HtmlParser in OpenPDF 2.4
//        val htmlReader = StringReader(content)
//        val parser = HtmlParser()
//        parser.parse(document, htmlReader)
//
//        document.close()

        val xhtml = Jsoup.parse(content).apply {
            outputSettings().syntax(org.jsoup.nodes.Document.OutputSettings.Syntax.xml)
        }.html()

        ITextRenderer().apply {
            setDocumentFromString(xhtml)
            layout()
            createPDF(initialPdf)
            finishPDF()
        }
//
//        // Add bookmarks in memory using PdfStamper
//        val finalPdf = ByteArrayOutputStream()
//        val reader = PdfReader(ByteArrayInputStream(initialPdf.toByteArray()))
//        val stamper = PdfStamper(reader, finalPdf)
//
//        val bookmarks = events.mapIndexed { idx, event ->
//            hashMapOf<String, Any>(
//                "Title" to event.event.title,
//                "Action" to "GoTo",
//                "Page" to "${idx + 1} Fit"
//            )
//        }
//
//        stamper.setOutlines(bookmarks)
//        stamper.close()
//        reader.close()

        return initialPdf
    }

}