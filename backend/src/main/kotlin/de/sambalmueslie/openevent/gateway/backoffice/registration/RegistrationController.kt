package de.sambalmueslie.openevent.gateway.backoffice.registration

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationDetails
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/registration")
@Tag(name = "BACKOFFICE Registration API")
class RegistrationController(
    private val service: RegistrationCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "registration.read"
        private const val PERMISSION_WRITE = "registration.write"
        private const val PERMISSION_ADMIN = "registration.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Registration API")

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Registration? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }
    }

    @Get("/{id}/info")
    fun getInfo(auth: Authentication, id: Long): RegistrationInfo? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getInfo(id) }
    }

    @Get("{id}/details")
    fun getDetails(auth: Authentication, id: Long): RegistrationDetails? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getDetails(id) }
    }

    @Post("/{id}/participant/account/{accountId}")
    fun addParticipant(
        auth: Authentication,
        id: Long,
        accountId: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val actor = accountService.get(auth) ?: throw InvalidRequestException("Cannot find user account")
            val account = accountService.get(accountId) ?: throw InvalidRequestException("Cannot find account [$accountId]")
            logger.traceAction(auth, "removeParticipant", id.toString()) {
                service.addParticipant(actor, id, account, request)
            }
        }
    }

    @Post("/{id}/participant/manual")
    fun addParticipant(
        auth: Authentication,
        id: Long,
        @Body request: ParticipantAddRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.findByEmail(request.email)
            logger.traceAction(auth, "addParticipant", id.toString(), request) {
                if (account != null) {
                    service.addParticipant(accountService.find(auth), id, account, ParticipateRequest(request.size))
                } else {
                    service.addParticipant(accountService.find(auth), id, request)
                }
            }
        }
    }

    @Put("/{id}/participant/{participantId}")
    fun changeParticipant(
        auth: Authentication,
        id: Long,
        participantId: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceAction(auth, "changeParticipant", participantId.toString(), request) {
                service.changeParticipant(accountService.find(auth), id, participantId, request)
            }
        }
    }

    @Delete("/{id}/participant/{participantId}")
    fun removeParticipant(auth: Authentication, id: Long, participantId: Long): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceAction(auth, "removeParticipant", participantId.toString()) {
                service.removeParticipant(accountService.find(auth), id, participantId)
            }
        }
    }
}