package de.sambalmueslie.openevent.gateway.backoffice.address

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.address.AddressCrudService
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/address")
@Tag(name = "BACKOFFICE Address API")
class AddressController(
    private val service: AddressCrudService,
    private val accountService: AccountCrudService,

    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "address.read"
        private const val PERMISSION_WRITE = "address.write"
        private const val PERMISSION_ADMIN = "address.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Address API")

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Address? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Page<Address> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getAll(pageable)
        }
    }

    @Post()
    fun create(auth: Authentication, @Body request: AddressChangeRequest): Address {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) {
                val account = accountService.find(auth)
                service.create(account, account, request)
            }
        }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AddressChangeRequest): Address {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long): Address? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

    @Post("/import")
    fun importLocations(auth: Authentication): Page<Address> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.importLocations(account)
        }
    }
}