package de.sambalmueslie.openevent.gateway.portal.address

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.address.AddressCrudService
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.error.IllegalAccessException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditAction
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class AddressGuardService(
    private val service: AddressCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "address.read"
        private const val PERMISSION_WRITE = "address.write"
    }

    private val logger = audit.getLogger("APP Address API")

    fun get(auth: Authentication, pageable: Pageable): Page<Address> =
        auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.getAllForAccount(account, pageable)
        }

    fun get(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.getForAccount(account, id)
        }

    fun importLocations(auth: Authentication): Page<Address> =
        auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.importLocations(account)
        }

    fun create(auth: Authentication, request: AddressChangeRequest) =
        auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) {
                val account = accountService.find(auth)
                service.create(account, account, request)
            }
        }

    fun update(auth: Authentication, id: Long, request: AddressChangeRequest) =
        auth.checkPermission(PERMISSION_WRITE) {
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

    fun delete(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.find(auth)
            val address = service.getData(id) ?: return@checkPermission null
            if (address.accountId == account.id) {
                logger.traceDelete(auth) { service.delete(account, id) }
            } else {
                throw IllegalAccessException("Cannot access address cause user is not author")
            }
        }

    fun setDefault(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.find(auth)
            val address = service.getData(id) ?: return@checkPermission null
            if (address.accountId == account.id) {
                logger.traceAction(auth, AuditAction.ADDRESS_SET_DEFAULT, "$id") { service.setDefault(account, id) }
            } else {
                throw IllegalAccessException("Cannot access address cause user is not author")
            }
        }
}
