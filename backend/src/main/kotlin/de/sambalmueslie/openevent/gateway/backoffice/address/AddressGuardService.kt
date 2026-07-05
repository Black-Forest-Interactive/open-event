package de.sambalmueslie.openevent.gateway.backoffice.address

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.address.AddressCrudService
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
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
        private const val PERMISSION_ADMIN = "address.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Address API")

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun create(auth: Authentication, request: AddressChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) {
                val account = accountService.find(auth)
                service.create(account, account, request)
            }
        }

    fun update(auth: Authentication, id: Long, request: AddressChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }

    fun delete(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }

    fun importLocations(auth: Authentication): Page<Address> =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.importLocations(account)
        }
}
