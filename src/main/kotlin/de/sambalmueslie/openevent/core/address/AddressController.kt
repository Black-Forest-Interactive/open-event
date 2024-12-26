package de.sambalmueslie.openevent.core.address


import de.sambalmueslie.openevent.api.AddressAPI
import de.sambalmueslie.openevent.api.AddressAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.LocationAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.LocationAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.getRealmRoles
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/address")
@Tag(name = "Address API")
class AddressController(
    private val service: AddressCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : AddressAPI {
    private val logger = audit.getLogger("Address API")

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Address? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { service.get(id) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Address> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.getAll(pageable)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission Page.empty()
                service.getAllForAccount(account, pageable)
            }
        }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: AddressChangeRequest): Address {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) {
                val account = accountService.find(auth)
                service.create(account, account, request)
            }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: AddressChangeRequest): Address {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Address? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

    @Post("/import")
    override fun importLocations(auth: Authentication): Page<Address> {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.importLocations(account)
        }
    }

    @Get("/for/account/{accountId}")
    override fun getForAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Address> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
                service.getAllForAccount(account, pageable)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission Page.empty()
                service.getAllForAccount(account, pageable)
            }
        }
    }

    private fun isAdmin(auth: Authentication) = auth.getRealmRoles().contains(PERMISSION_ADMIN)
}
