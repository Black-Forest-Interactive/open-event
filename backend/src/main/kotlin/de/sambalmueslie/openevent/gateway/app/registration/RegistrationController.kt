package de.sambalmueslie.openevent.gateway.app.registration

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.RegistrationDetails
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.gateway.app.event.EventGuardService
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/registration")
@Tag(name = "APP Registration API")
class RegistrationController(
    private val service: RegistrationCrudService,
    private val eventService: EventGuardService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "registration.read"
        private const val PERMISSION_WRITE = "registration.write"
    }

    private val logger = audit.getLogger("APP Registration API")

    @Get("/{id}/participant")
    fun getParticipants(auth: Authentication, id: Long): List<Participant> {
        return auth.checkPermission(PERMISSION_READ) { service.getParticipants(id) }
    }

    @Post("/{id}/participant")
    fun addParticipant(auth: Authentication, id: Long, @Body request: ParticipateRequest): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "addParticipant", id.toString(), request) {
                service.addParticipant(account, id, account, request)
            }
        }
    }

    @Put("/{id}/participant")
    fun changeParticipant(
        auth: Authentication,
        id: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "changeParticipant", id.toString(), request) {
                service.changeParticipant(account, id, account, request)
            }
        }
    }

    @Delete("/{id}/participant")
    fun removeParticipant(auth: Authentication, id: Long): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "removeParticipant", id.toString(), "") {
                service.removeParticipant(account, id, account)
            }
        }
    }


    @Post("/{id}/participant/manual")
    fun moderationParticipateManual(
        auth: Authentication,
        id: Long,
        @Body request: ParticipantAddRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, actor) = eventService.getIfAccessible(auth, id) ?: return@checkPermission null
            val account = accountService.findByEmail(request.email)
            logger.traceAction(auth, "addParticipant", id.toString(), request) {
                if (account != null) {
                    service.addParticipant(actor, event.id, account, ParticipateRequest(request.size))
                } else {
                    service.addParticipant(actor, event.id, request)
                }
            }
        }
    }

    @Put("/{id}/participant/{participantId}")
    fun moderationChangeParticipant(
        auth: Authentication,
        id: Long,
        participantId: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, actor) = eventService.getIfAccessible(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, "changeParticipant", participantId.toString(), request) {
                service.changeParticipant(actor, event.id, participantId, request)
            }
        }
    }

    @Delete("/{id}/participant/{participantId}")
    fun moderationRemoveParticipant(auth: Authentication, id: Long, participantId: Long): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, actor) = eventService.getIfAccessible(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, "removeParticipant", participantId.toString()) {
                service.removeParticipant(actor, event.id, participantId)
            }
        }
    }


    @Get("{id}/details")
    fun getDetails(auth: Authentication, id: Long): RegistrationDetails? {
        return auth.checkPermission(PERMISSION_READ) {
            val (event, _) = eventService.getIfAccessible(auth, id) ?: return@checkPermission null
            service.getDetails(event.id)
        }
    }
}