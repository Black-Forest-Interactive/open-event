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
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class AccountGuardService(
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

    fun validate(auth: Authentication, lang: String) = auth.checkPermission(PERMISSION_ADMIN) { service.validate(auth, lang) }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getProfile(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            service.getProfile(account)
        }

    fun getPreferences(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            service.getPreferences(account)
        }

    fun getAddress(auth: Authentication, id: Long, pageable: Pageable): Page<Address>? =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            addressCrudService.getAllForAccount(account, pageable)
        }

    fun createAddress(auth: Authentication, id: Long, request: AddressChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            logger.traceCreate(auth, request) {
                addressCrudService.create(account, account, request)
            }
        }

    fun importLocations(auth: Authentication, id: Long): Page<Address> =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission Page.empty()
            addressCrudService.importLocations(account)
        }

    fun getEvent(auth: Authentication, id: Long, pageable: Pageable): Page<Event>? =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            eventCrudService.getOwned(account, pageable)
        }

    fun createEvent(auth: Authentication, id: Long, request: EventChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = service.get(id) ?: return@checkPermission null
            logger.traceCreate(auth, request) {
                eventCrudService.create(account, request)
            }
        }

    fun search(auth: Authentication, request: AccountSearchRequest, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val system = service.getSystemAccount()
            searchService.searchAccounts(system, request, pageable)
        }

    fun create(auth: Authentication, request: AccountChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(service.find(auth), request) }
        }

    fun update(auth: Authentication, id: Long, request: AccountChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) { service.update(service.find(auth), id, request) }
        }

    fun delete(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(service.find(auth), id) }
        }

    fun setup(auth: Authentication, request: AccountSetupRequest) =
        auth.checkPermission(PERMISSION_ADMIN) { service.setup(service.find(auth), request) }

    fun updateSetup(auth: Authentication, id: Long, request: AccountSetupRequest) =
        auth.checkPermission(PERMISSION_ADMIN) { service.update(service.find(auth), id, request) }
}
