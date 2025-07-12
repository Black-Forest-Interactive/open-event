package de.sambalmueslie.openevent.gateway.app.address

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.address.AddressCrudService
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.error.IllegalAccessException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/address")
@Tag(name = "APP Address API")
class AddressController(
    private val service: AddressCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "address.read"
        private const val PERMISSION_WRITE = "address.write"
    }


    private val logger = audit.getLogger("APP Address API")

    @Get()
    fun get(auth: Authentication, pageable: Pageable): Page<Address> {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.getAllForAccount(account, pageable)
        }
    }

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Address? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.getForAccount(account, id)
        }
    }

    @Post("/import")
    fun importLocations(auth: Authentication): Page<Address> {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.importLocations(account)
        }
    }


    @Post()
    fun create(auth: Authentication, @Body request: AddressChangeRequest): Address {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) {
                val account = accountService.find(auth)
                service.create(account, account, request)
            }
        }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AddressChangeRequest): Address {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.find(auth)
            val address = service.getData(id)
            if (address == null) {
                logger.traceCreate(auth, request) { service.create(account, account, request) }
            } else if (address.accountId == account.id) {
                logger.traceUpdate(auth, request) { service.update(account, id, request) }
            } else {
                throw IllegalAccessException("Cannot access address cause user is not author")
            }
        }
    }

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long): Address? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.find(auth)
            val address = service.getData(id) ?: return@checkPermission null
            if (address.accountId == account.id) {
                logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
            } else {
                throw IllegalAccessException("Cannot access address cause user is not author")
            }
        }
    }
}