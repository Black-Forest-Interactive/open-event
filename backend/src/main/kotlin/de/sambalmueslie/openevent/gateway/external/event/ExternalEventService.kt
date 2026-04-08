package de.sambalmueslie.openevent.gateway.external.event

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.link.LinkCrudService
import de.sambalmueslie.openevent.core.participant.ExternalParticipantService
import de.sambalmueslie.openevent.core.participant.api.*
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.event.EventSearchEntryData
import de.sambalmueslie.openevent.core.share.ShareCrudService
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.gateway.external.account.toPublicAccount
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class ExternalEventService(
    private val service: EventCrudService,
    private val shareService: ShareCrudService,
    private val linkService: LinkCrudService,
    private val searchService: SearchService,
    private val participantService: ExternalParticipantService,
    private val settingsService: SettingsService,
    private val accountService: AccountCrudService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ExternalEventService::class.java)
    }


    fun getPublicEvent(id: String): PublicEvent? {
        val (share, event) = getEvent(id) ?: return null
        return event.toPublicEvent(share)
    }

    fun getSettings(): EventParticipationSettings {
        return EventParticipationSettings(settingsService.getValidateRegistrationCode())
    }

    fun requestParticipation(id: String, request: ExternalParticipantAddRequest, lang: String): ExternalParticipantChangeResponse {
        val (share, event) = getEvent(id) ?: return ExternalParticipantChangeResponse.failed()
        logger.info("[${event.event.id}] request participation $request")
        return participantService.requestParticipation(share, event, request, lang)
    }

    fun changeParticipation(id: String, participantId: String, request: ExternalParticipantChangeRequest): ExternalParticipantChangeResponse {
        val (_, event) = getEvent(id) ?: return ExternalParticipantChangeResponse.failed()
        logger.info("[${event.event.id}] change participation [$participantId] $request")
        return participantService.changeParticipation(event, participantId, request)
    }

    fun cancelParticipation(id: String, participantId: String): ExternalParticipantChangeResponse {
        val (_, event) = getEvent(id) ?: return ExternalParticipantChangeResponse.failed()
        logger.info("[${event.event.id}] cancel participation [$participantId]")
        return participantService.cancelParticipation(event, participantId)
    }

    fun confirmParticipation(id: String, participantId: String, request: ExternalParticipantConfirmRequest): ExternalParticipantConfirmResponse {
        val (_, event) = getEvent(id) ?: return ExternalParticipantConfirmResponse.failed()
        logger.info("[${event.event.id}] confirm participation [$participantId]")
        return participantService.confirmParticipation(event, participantId, request)
    }

    private fun getEvent(id: String): Pair<Share, EventInfo>? {
        val share = shareService.get(id) ?: return null
        if (!share.enabled) return null
        val event = service.getInfo(share.eventId, null) ?: return null
        if (!event.event.published) return null
        return Pair(share, event)
    }

    fun search(request: PublicEventSearchRequest, key: String, pageable: Pageable): Page<PublicEvent> {
        val link = linkService.findByKey(settingsService.getPublicEventListKey()) ?: return Page.empty()
        if (link.id != key) return Page.empty()
        val systemAccount = accountService.getSystemAccount()
        val response = searchService.searchEvents(systemAccount, request.convert(), pageable)
        val eventIds = response.result.mapNotNull { it.id.toLongOrNull() }.toSet()

        val shares = shareService.findByEventIds(eventIds).filter { it.enabled }.associateBy { it.eventId }
        val events = service.getInfoByIds(eventIds).filter { it.event.published }

        val result = events.mapNotNull { e -> e.convert(shares[e.event.id]) }

        return Page.of(result, response.result.pageable, response.result.totalSize)
    }

    private fun PublicEventSearchRequest.convert() = EventSearchRequest(fullTextSearch, from, to, false, false, onlyAvailableEvents)

    private fun EventInfo.convert(share: Share?): PublicEvent? {
        return if (share != null) this.toPublicEvent(share) else null
    }

    private fun EventInfo.toPublicEvent(share: Share): PublicEvent {
        val entry = EventSearchEntryData.create(this)
        return PublicEvent(
            share.id,
            event.start,
            event.finish,
            event.title,
            event.shortText,
            event.longText,
            event.owner.toPublicAccount(),

            event.hasLocation,
            entry.zip ?: "",
            entry.city ?: "",
            entry.country ?: "",

            entry.hasSpaceLeft,
            entry.maxGuestAmount,
            entry.amountAccepted,
            entry.amountOnWaitingList,
            entry.remainingSpace,

            entry.categories,
            entry.tags
        )
    }

}