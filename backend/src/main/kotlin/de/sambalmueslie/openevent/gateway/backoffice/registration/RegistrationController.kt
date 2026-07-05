package de.sambalmueslie.openevent.gateway.backoffice.registration

import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/registration")
@Tag(name = "BACKOFFICE Registration API")
class RegistrationController(private val service: RegistrationGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get("/{id}/info")
    fun getInfo(auth: Authentication, id: Long) = service.getInfo(auth, id)

    @Get("{id}/details")
    fun getDetails(auth: Authentication, id: Long) = service.getDetails(auth, id)

    @Post("/{id}/participant/account/{accountId}")
    fun addParticipant(
        auth: Authentication,
        id: Long,
        accountId: Long,
        @Body request: ParticipateRequest
    ) = service.addParticipant(auth, id, accountId, request)

    @Post("/{id}/participant/manual")
    fun addParticipant(
        auth: Authentication,
        id: Long,
        @Body request: ParticipantAddRequest
    ) = service.addParticipant(auth, id, request)

    @Put("/{id}/participant/{participantId}")
    fun changeParticipant(
        auth: Authentication,
        id: Long,
        participantId: Long,
        @Body request: ParticipateRequest
    ) = service.changeParticipant(auth, id, participantId, request)

    @Delete("/{id}/participant/{participantId}")
    fun removeParticipant(auth: Authentication, id: Long, participantId: Long) = service.removeParticipant(auth, id, participantId)
}
