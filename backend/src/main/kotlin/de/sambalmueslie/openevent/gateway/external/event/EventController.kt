package de.sambalmueslie.openevent.gateway.external.event

import de.sambalmueslie.openevent.core.participant.api.*
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/external/event")
@Tag(name = "Public Event API")
@Secured(SecurityRule.IS_ANONYMOUS)
class EventController(
    private val service: ExternalEventService
) {

    @Get("{id}")
    fun get(id: String): PublicEvent? {
        return service.getPublicEvent(id)
    }

    @Post("/{id}/participant")
    fun requestParticipation(id: String, @Body request: ExternalParticipantAddRequest, @QueryValue(defaultValue = "") lang: String): ExternalParticipantChangeResponse {
        return service.requestParticipation(id, request, lang)
    }


    @Put("/{id}/participant/{participantId}")
    fun changeParticipation(id: String, participantId: String, @Body request: ExternalParticipantChangeRequest): ExternalParticipantChangeResponse {
        return service.changeParticipation(id, participantId, request)
    }

    @Delete("/{id}/participant/{participantId}")
    fun cancelParticipation(id: String, participantId: String): ExternalParticipantChangeResponse {
        return service.cancelParticipation(id, participantId)
    }

    @Post("/{id}/participant/{participantId}/confirm")
    fun confirmParticipation(id: String, participantId: String, @Body request: ExternalParticipantConfirmRequest): ExternalParticipantConfirmResponse {
        return service.confirmParticipation(id, participantId, request)
    }

}