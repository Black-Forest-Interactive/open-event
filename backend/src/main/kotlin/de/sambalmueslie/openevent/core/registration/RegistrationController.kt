package de.sambalmueslie.openevent.core.registration


import de.sambalmueslie.openevent.api.RegistrationAPI
import de.sambalmueslie.openevent.api.RegistrationAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.RegistrationAPI.Companion.PERMISSION_MANAGE
import de.sambalmueslie.openevent.api.RegistrationAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.RegistrationAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.CoreAPI
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest
import de.sambalmueslie.openevent.core.registration.api.RegistrationDetails
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/registration")
@Tag(name = "Registration API")
@CoreAPI
class RegistrationController(
    private val service: RegistrationCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : RegistrationAPI {
    private val logger = audit.getLogger("Registration API")

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Registration? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get("/{id}/info")
    override fun getInfo(auth: Authentication, id: Long): RegistrationInfo? {
        return auth.checkPermission(PERMISSION_READ) { service.getInfo(id) }
    }

    @Get("{id}/details")
    override fun getDetails(auth: Authentication, id: Long): RegistrationDetails? {
        return auth.checkPermission(PERMISSION_MANAGE, PERMISSION_ADMIN) { service.getDetails(id) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Registration> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: RegistrationChangeRequest): Registration {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: RegistrationChangeRequest): Registration {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Registration? {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

    @Get("/{id}/participant")
    override fun getParticipants(auth: Authentication, id: Long): List<Participant> {
        return auth.checkPermission(PERMISSION_READ) { service.getParticipants(id) }
    }

    @Post("/{id}/participant")
    override fun addParticipant(
        auth: Authentication,
        id: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "addParticipant", id.toString(), request) {
                service.addParticipant(account, id, account, request)
            }
        }
    }

    @Put("/{id}/participant")
    override fun changeParticipant(
        auth: Authentication,
        id: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "changeParticipant", id.toString(), request) {
                service.changeParticipant(account, id, account, request)
            }
        }
    }

    @Delete("/{id}/participant")
    override fun removeParticipant(auth: Authentication, id: Long): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "removeParticipant", id.toString(), "") {
                service.removeParticipant(account, id, account)
            }
        }
    }


    @Post("/{id}/participant/account/{accountId}")
    override fun addParticipant(
        auth: Authentication,
        id: Long,
        accountId: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_MANAGE, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            logger.traceAction(auth, "removeParticipant", id.toString()) {
                service.addParticipant(account, id, account, request)
            }
        }
    }

    @Post("/{id}/participant/manual")
    override fun addParticipant(
        auth: Authentication,
        id: Long,
        @Body request: ParticipantAddRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_MANAGE, PERMISSION_ADMIN) {
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
    override fun changeParticipant(
        auth: Authentication,
        id: Long,
        participantId: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_MANAGE, PERMISSION_ADMIN) {
            logger.traceAction(auth, "changeParticipant", participantId.toString(), request) {
                service.changeParticipant(accountService.find(auth), id, participantId, request)
            }
        }
    }

    @Delete("/{id}/participant/{participantId}")
    override fun removeParticipant(auth: Authentication, id: Long, participantId: Long): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_MANAGE, PERMISSION_ADMIN) {
            logger.traceAction(auth, "removeParticipant", participantId.toString()) {
                service.removeParticipant(accountService.find(auth), id, participantId)
            }
        }
    }


}
