package de.sambalmueslie.openevent.gateway.backoffice.account

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.*
import de.sambalmueslie.openevent.core.address.AddressCrudService
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.AccountSearchRequest
import de.sambalmueslie.openevent.core.search.api.AccountSearchResponse
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/account")
@Tag(name = "BACKOFFICE Account API")
class AccountController(
    private val service: AccountCrudService,
    private val addressCrudService: AddressCrudService,
    private val eventCrudService: EventCrudService,
    private val searchService: SearchService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "account.read"
        private const val PERMISSION_WRITE = "account.write"
        private const val PERMISSION_ADMIN = "account.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Account API")

    @Get("/validate")
    fun validate(auth: Authentication, @QueryValue lang: String): AccountValidationResult {
        return auth.checkPermission(PERMISSION_ADMIN) { service.validate(auth, lang) }
    }

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Account? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.get(id)
        }
    }

    @Get("/{id}/profile")
    fun getProfile(auth: Authentication, id: Long): Profile? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            service.getProfile(account)
        }
    }

    @Get("/{id}/preferences")
    fun getPreferences(auth: Authentication, id: Long): Preferences? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            service.getPreferences(account)
        }
    }

    @Get("/{id}/address")
    fun getAddress(auth: Authentication, id: Long, pageable: Pageable): Page<Address>? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            addressCrudService.getAllForAccount(account, pageable)
        }
    }

    @Post("/{id}/address")
    fun createAddress(auth: Authentication, id: Long, @Body request: AddressChangeRequest): Address? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            logger.traceCreate(auth, request) {
                addressCrudService.create(account, account, request)
            }
        }
    }

    @Post("/{id}/address/import")
    fun importLocations(auth: Authentication, id: Long): Page<Address> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission Page.empty()
            addressCrudService.importLocations(account)
        }
    }

    @Get("/{id}/event")
    fun getEvent(auth: Authentication, id: Long, pageable: Pageable): Page<Event>? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            eventCrudService.getOwned(account, pageable)
        }
    }

    @Post("/{id}/event")
    fun createEvent(auth: Authentication, id: Long, @Body request: EventChangeRequest): Event? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            logger.traceCreate(auth, request) {
                eventCrudService.create(account, request)
            }
        }
    }

    @Post("/search")
    fun search(auth: Authentication, @Body request: AccountSearchRequest, pageable: Pageable): AccountSearchResponse {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val system = service.getSystemAccount()
            searchService.searchAccounts(system, request, pageable)
        }
    }

    @Post()
    fun create(auth: Authentication, @Body request: AccountChangeRequest): Account {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(service.find(auth), request) }
        }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AccountChangeRequest): Account {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(service.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long): Account? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(service.find(auth), id) }
        }
    }

    @Post("/setup")
    fun setup(auth: Authentication, @Body request: AccountSetupRequest): AccountInfo {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.setup(service.find(auth), request)
        }
    }

    @Put("/setup/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AccountSetupRequest): AccountInfo {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.update(service.find(auth), id, request)
        }
    }
}