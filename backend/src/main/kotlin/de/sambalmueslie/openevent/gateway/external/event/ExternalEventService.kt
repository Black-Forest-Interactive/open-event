package de.sambalmueslie.openevent.gateway.external.event

import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.participant.ExternalParticipantService
import de.sambalmueslie.openevent.core.share.ShareCrudService
import de.sambalmueslie.openevent.gateway.external.participant.api.*
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class ExternalEventService(
    private val service: EventCrudService,
    private val shareService: ShareCrudService,
    private val participantService: ExternalParticipantService
) {
    companion object {
        private val logger = LoggerFactory.getLogger(ExternalEventService::class.java)
    }


    fun getPublicEvent(id: String): PublicEvent? {
        val share = shareService.get(id) ?: return null
        val event = service.getInfo(share.eventId, null) ?: return null
        return event.toPublicEvent(share)
    }

    fun requestParticipation(id: String, request: ExternalParticipantAddRequest, lang: String): ExternalParticipantChangeResponse {
        val event = getEvent(id) ?: return ExternalParticipantChangeResponse.failed()
        logger.info("[${event.event.id}] request participation $request")
        return participantService.requestParticipation(event, request, lang)
    }

    fun changeParticipation(id: String, participantId: String, request: ExternalParticipantChangeRequest): ExternalParticipantChangeResponse {
        val event = getEvent(id) ?: return ExternalParticipantChangeResponse.failed()
        logger.info("[${event.event.id}] change participation [$participantId] $request")
        return participantService.changeParticipation(event, participantId, request)
    }

    fun cancelParticipation(id: String, participantId: String): ExternalParticipantChangeResponse {
        val event = getEvent(id) ?: return ExternalParticipantChangeResponse.failed()
        logger.info("[${event.event.id}] cancel participation [$participantId]")
        return participantService.cancelParticipation(event, participantId)
    }

    fun confirmParticipation(id: String, participantId: String, request: ExternalParticipantConfirmRequest): ExternalParticipantConfirmResponse {
        val event = getEvent(id) ?: return ExternalParticipantConfirmResponse.failed()
        logger.info("[${event.event.id}] confirm participation [$participantId]")
        return participantService.confirmParticipation(event, participantId, request)
    }

    private fun getEvent(id: String): EventInfo? {
        val share = shareService.get(id) ?: return null
        return service.getInfo(share.eventId, null)
    }
}