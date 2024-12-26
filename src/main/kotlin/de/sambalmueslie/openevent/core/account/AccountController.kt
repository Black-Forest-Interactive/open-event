package de.sambalmueslie.openevent.core.account

import de.sambalmueslie.openevent.api.AccountAPI
import de.sambalmueslie.openevent.api.AccountAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.AccountAPI.Companion.PERMISSION_MODERATOR
import de.sambalmueslie.openevent.api.AccountAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.AccountAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.account.api.*
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/account")
@Tag(name = "Account API")
class AccountController(
    private val service: AccountCrudService,
    audit: AuditService,
) : AccountAPI {

    private val logger = audit.getLogger("Account API")

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Account? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }


    @Get("/find/by/name")
    override fun findByName(auth: Authentication, @QueryValue name: String, pageable: Pageable): Page<Account> {
        return auth.checkPermission(PERMISSION_READ) { service.findByName(name, pageable) }
    }

    @Get("/find/by/email")
    override fun findByEmail(auth: Authentication, @QueryValue email: String): Account? {
        return auth.checkPermission(PERMISSION_READ) { service.findByEmail(email) }
    }

    @Get("/find/by/externalId")
    override fun findByExternalId(auth: Authentication, @QueryValue externalId: String): Account? {
        return auth.checkPermission(PERMISSION_READ) { service.findByExternalId(externalId) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Account> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: AccountChangeRequest): Account {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) { service.create(service.find(auth), request) }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: AccountChangeRequest): Account {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceUpdate(auth, request) { service.update(service.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Account? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(service.find(auth), id) }
        }
    }

    @Get("/validate")
    override fun validate(auth: Authentication, @QueryValue lang: String): AccountValidationResult {
        return auth.checkPermission(PERMISSION_READ) { service.validate(auth, lang) }
    }

    @Get("/profile")
    override fun getProfile(auth: Authentication): Profile? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = service.get(auth) ?: return@checkPermission null
            service.getProfile(account)
        }
    }

    @Get("/preferences")
    override fun getPreferences(auth: Authentication): Preferences? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = service.get(auth) ?: return@checkPermission null
            service.getPreferences(account)
        }
    }

    @Post("/setup")
    override fun setup(auth: Authentication, @Body request: AccountSetupRequest): AccountInfo? {
        return auth.checkPermission(PERMISSION_ADMIN, PERMISSION_MODERATOR) {
            val account = service.get(auth) ?: return@checkPermission null
            service.setup(account, request)
        }
    }

    @Put("/setup/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: AccountSetupRequest): AccountInfo? {
        return auth.checkPermission(PERMISSION_ADMIN, PERMISSION_MODERATOR) {
            val account = service.get(auth) ?: return@checkPermission null
            service.update(account, id, request)
        }
    }

}
