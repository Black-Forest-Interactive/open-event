package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.link.LinkCrudService
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventCardPdfExporter(
    private val settingsService: SettingsService,
    private val linkService: LinkCrudService,
    timeProvider: TimeProvider
) : BasePdfExporter("templates/event-cards-v1.vm", settingsService, timeProvider, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventCardPdfExporter::class.java)
    }

    override fun exportEvents(provider: () -> Sequence<EventInfo>) =
        renderPdfFile(provider.invoke().toList(), portalProperties())

    override fun exportEvent(info: EventInfo) = renderPdfFile(listOf(info), portalProperties())

    private fun portalProperties(): Map<String, Any> {
        val publicEventListLink = linkService.findByKey(settingsService.getPublicEventListKey()) ?: return emptyMap()
        val publicEventListUrl = "${settingsService.getShareUrl()}/event/${publicEventListLink.id}/search"
        return mapOf(
            Pair("portalQrCode", createQrCodeFromUrl(publicEventListUrl)),
            Pair("portalUrl", publicEventListUrl)
        )
    }
}
