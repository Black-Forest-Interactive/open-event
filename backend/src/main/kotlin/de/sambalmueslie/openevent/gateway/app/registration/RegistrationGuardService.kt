package de.sambalmueslie.openevent.gateway.app.registration

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationDetails
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.gateway.app.event.EventGuardService
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class RegistrationGuardService(
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

    fun getParticipants(auth: Authentication, id: Long): List<Participant> {
        return auth.checkPermission(PERMISSION_READ) { service.getParticipants(id) }
    }

    fun addParticipant(auth: Authentication, id: Long, request: ParticipateRequest): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "addParticipant", id.toString(), request) {
                service.addParticipant(account, id, account, request)
            }
        }
    }

    fun changeParticipant(auth: Authentication, id: Long, request: ParticipateRequest): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "changeParticipant", id.toString(), request) {
                service.changeParticipant(account, id, account, request)
            }
        }
    }

    fun removeParticipant(auth: Authentication, id: Long): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "removeParticipant", id.toString(), "") {
                service.removeParticipant(account, id, account)
            }
        }
    }

    fun moderationParticipateManual(auth: Authentication, id: Long, request: ParticipantAddRequest): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (registration, _, actor) = getIfAccessible(auth, id) ?: return@checkPermission null
            val account = accountService.findByEmail(request.email)
            logger.traceAction(auth, "addParticipant", id.toString(), request) {
                if (account != null) {
                    service.addParticipant(actor, registration.id, account, ParticipateRequest(request.size))
                } else {
                    service.addParticipant(actor, registration.id, request)
                }
            }
        }
    }

    fun moderationChangeParticipant(auth: Authentication, id: Long, participantId: Long, request: ParticipateRequest): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (registration, _, actor) = getIfAccessible(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, "changeParticipant", participantId.toString(), request) {
                service.changeParticipant(actor, registration.id, participantId, request)
            }
        }
    }

    fun moderationRemoveParticipant(auth: Authentication, id: Long, participantId: Long): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (registration, _, actor) = getIfAccessible(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, "removeParticipant", participantId.toString()) {
                service.removeParticipant(actor, registration.id, participantId)
            }
        }
    }

    fun getDetails(auth: Authentication, id: Long): RegistrationDetails? {
        return auth.checkPermission(PERMISSION_READ) {
            val (registration, _, _) = getIfAccessible(auth, id) ?: return@checkPermission null
            service.getDetails(registration.id)
        }
    }

    fun getIfAccessible(auth: Authentication, id: Long): Triple<Registration, Event, Account>? {
        val registration = service.get(id) ?: return null
        val (event, account) = eventService.getIfAccessible(auth, registration.eventId) ?: return null
        return Triple(registration, event, account)
    }
}