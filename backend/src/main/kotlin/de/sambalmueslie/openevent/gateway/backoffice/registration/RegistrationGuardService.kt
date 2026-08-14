package de.sambalmueslie.openevent.gateway.backoffice.registration

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditAction
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class RegistrationGuardService(
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

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getInfo(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.getInfo(id) }

    fun getDetails(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.getDetails(id) }

    fun addParticipant(auth: Authentication, id: Long, accountId: Long, request: ParticipateRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val actor = accountService.get(auth) ?: throw InvalidRequestException("Cannot find user account")
            val account = accountService.get(accountId) ?: throw InvalidRequestException("Cannot find account [$accountId]")
            logger.traceAction(auth, AuditAction.REGISTRATION_PARTICIPANT_ADDED, id.toString()) {
                service.addParticipant(actor, id, account, request)
            }
        }

    fun addParticipant(auth: Authentication, id: Long, request: ParticipantAddRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.findByEmail(request.email)
            logger.traceAction(auth, AuditAction.REGISTRATION_PARTICIPANT_ADDED, id.toString(), request) {
                if (account != null) {
                    service.addParticipant(accountService.find(auth), id, account, ParticipateRequest(request.size, request.note))
                } else {
                    service.addParticipant(accountService.find(auth), id, request)
                }
            }
        }

    fun changeParticipant(auth: Authentication, id: Long, participantId: Long, request: ParticipateRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceAction(auth, AuditAction.REGISTRATION_PARTICIPANT_CHANGED, participantId.toString(), request) {
                service.changeParticipant(accountService.find(auth), id, participantId, request)
            }
        }

    fun removeParticipant(auth: Authentication, id: Long, participantId: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceAction(auth, AuditAction.REGISTRATION_PARTICIPANT_REMOVED, participantId.toString()) {
                service.removeParticipant(accountService.find(auth), id, participantId)
            }
        }
}
