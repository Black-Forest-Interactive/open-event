package de.sambalmueslie.openevent.gateway.external.registration

import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/public/registration")
@Tag(name = "Public Registration API")
class RegistrationController {


    @Post("/{id}/participant")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun requestParticipation(
        auth: Authentication?,
        id: String,
        @Body request: ParticipantAddRequest
    ) {
        TODO("not implemented yet")
    }


    @Put("/{id}/participant")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun changeParticipation(
        auth: Authentication,
        id: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        TODO("not implemented yet")
    }

    @Delete("/{id}/participant")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun cancelParticipation(auth: Authentication, id: Long): ParticipateResponse? {
        TODO("not implemented yet")
    }

    @Post("/{id}/participant")
    @Secured(SecurityRule.IS_ANONYMOUS)
    fun confirmParticipation(
        auth: Authentication?,
        id: String,
        @Body request: ParticipantConfirmRequest
    ) {
        TODO("not implemented yet")
    }
}