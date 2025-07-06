package de.sambalmueslie.openevent.gateway.app.registration

import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.core.registration.api.RegistrationDetails
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/registration")
@Tag(name = "APP Registration API")
class RegistrationController(private val service: RegistrationGuardService) {


    @Get("/{id}/participant")
    fun getParticipants(auth: Authentication, id: Long): List<Participant> = service.getParticipants(auth, id)

    @Post("/{id}/participant")
    fun addParticipant(auth: Authentication, id: Long, @Body request: ParticipateRequest): ParticipateResponse? = service.addParticipant(auth, id, request)

    @Put("/{id}/participant")
    fun changeParticipant(auth: Authentication, id: Long, @Body request: ParticipateRequest): ParticipateResponse? = service.changeParticipant(auth, id, request)

    @Delete("/{id}/participant")
    fun removeParticipant(auth: Authentication, id: Long): ParticipateResponse? = service.removeParticipant(auth, id)

    @Post("/{id}/participant/manual")
    fun moderationParticipateManual(auth: Authentication, id: Long, @Body request: ParticipantAddRequest): ParticipateResponse? = service.moderationParticipateManual(auth, id, request)

    @Put("/{id}/participant/{participantId}")
    fun moderationChangeParticipant(auth: Authentication, id: Long, participantId: Long, @Body request: ParticipateRequest): ParticipateResponse? =
        service.moderationChangeParticipant(auth, id, participantId, request)

    @Delete("/{id}/participant/{participantId}")
    fun moderationRemoveParticipant(auth: Authentication, id: Long, participantId: Long): ParticipateResponse? = service.moderationRemoveParticipant(auth, id, participantId)

    @Get("{id}/details")
    fun getDetails(auth: Authentication, id: Long): RegistrationDetails? = service.getDetails(auth, id)
}