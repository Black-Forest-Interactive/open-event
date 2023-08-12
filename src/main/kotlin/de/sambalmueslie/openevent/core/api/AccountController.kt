package de.sambalmueslie.openevent.core.api

import de.sambalmueslie.openevent.api.AccountAPI
import de.sambalmueslie.openevent.api.AccountAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.AccountAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.AccountAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.account.AccountSearchService
import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.AccountChangeRequest
import de.sambalmueslie.openevent.core.model.AccountValidationResult
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/account")
@Tag(name = "Account API")
class AccountController(
    private val service: AccountCrudService,
    private val search: AccountSearchService
) : AccountAPI {

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
    override fun findByExternalId(auth: Authentication, @QueryValue nexternalId: String): Account? {
        return auth.checkPermission(PERMISSION_READ) { service.findByExternalId(nexternalId) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Account> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: AccountChangeRequest): Account {
        return auth.checkPermission(PERMISSION_WRITE) { service.create(request) }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: AccountChangeRequest): Account {
        return auth.checkPermission(PERMISSION_WRITE) { service.update(id, request) }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Account? {
        return auth.checkPermission(PERMISSION_WRITE) { service.delete(id) }
    }

    @Get("/validate")
    override fun validate(auth: Authentication): AccountValidationResult {
        return auth.checkPermission(PERMISSION_READ) { service.validate(auth) }
    }


    @Get("/search")
    override fun search(auth: Authentication, @QueryValue query: String, pageable: Pageable): Page<Account> {
        return auth.checkPermission(PERMISSION_READ) { search.search(query, pageable) }
    }

    @Post("/search")
    fun buildIndex(auth: Authentication) {
        return auth.checkPermission(PERMISSION_ADMIN) {
            search.createIndex()
            HttpResponse.created("")
        }

    }

}
