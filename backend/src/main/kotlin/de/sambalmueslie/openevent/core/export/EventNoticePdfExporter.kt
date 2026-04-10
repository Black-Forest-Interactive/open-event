package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.link.LinkCrudService
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.http.server.types.files.SystemFile
import jakarta.inject.Singleton
import org.jsoup.Jsoup
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.xhtmlrenderer.pdf.ITextRenderer
import java.io.ByteArrayOutputStream

@Singleton
class EventNoticePdfExporter(
    private val settingsService: SettingsService,
    private val linkService: LinkCrudService,
    timeProvider: TimeProvider
) : BasePdfExporter("templates/event-notice-v1.vm", settingsService, timeProvider, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventNoticePdfExporter::class.java)
    }

    override fun exportEvents(provider: () -> Sequence<EventInfo>): SystemFile? {
        val infos = provider.invoke().toList().filter { it.share != null }

        val publicEventListLink = linkService.findByKey(settingsService.getPublicEventListKey()) ?: return null
        val externalBaseUrl = settingsService.getShareUrl()
        val publicEventListUrl = "$externalBaseUrl/${publicEventListLink.id}/search"
        val properties = mapOf(
            Pair("portalQrCode", createQrCodeFromUrl(publicEventListUrl)),
            Pair("portalUrl", publicEventListUrl)
        )

        return renderPdfFile(infos, properties)
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
