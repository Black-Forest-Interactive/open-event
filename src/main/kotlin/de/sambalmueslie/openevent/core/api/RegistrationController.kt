package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.RegistrationAPI
import de.sambalmueslie.openevent.api.RegistrationAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.RegistrationAPI.Companion.PERMISSION_MANAGE
import de.sambalmueslie.openevent.api.RegistrationAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.RegistrationAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.error.InvalidRequestException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/registration")
@Tag(name = "Registration API")
class RegistrationController(
    private val service: RegistrationCrudService,
    private val accountService: AccountCrudService
) : RegistrationAPI {

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Registration? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Registration> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: RegistrationChangeRequest): Registration {
        return auth.checkPermission(PERMISSION_WRITE) { service.create(request) }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: RegistrationChangeRequest): Registration {
        return auth.checkPermission(PERMISSION_WRITE) { service.update(id, request) }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Registration? {
        return auth.checkPermission(PERMISSION_WRITE) { service.delete(id) }
    }

    @Get("/{id}/participant")
    override fun getParticipants(auth: Authentication, id: Long): List<Participant> {
        return auth.checkPermission(PERMISSION_READ) { service.getParticipants(id) }
    }

    @Put("/{id}/participant")
    override fun addParticipant(auth: Authentication, id: Long, @Body request: ParticipateRequest): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            service.addParticipant(id, account, request)
        }
    }

    @Put("/{id}/participant/{accountId}")
    override fun addParticipant(
        auth: Authentication,
        id: Long,
        accountId: Long,
        @Body request: ParticipateRequest
    ): ParticipateResponse? {
        return auth.checkPermission(PERMISSION_MANAGE, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: throw InvalidRequestException("Cannot find account")
            service.addParticipant(id, account, request)
        }
    }

}
