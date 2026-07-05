package de.sambalmueslie.openevent.gateway.backoffice.event

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.event.api.EventStats
import de.sambalmueslie.openevent.core.history.HistoryCrudService
import de.sambalmueslie.openevent.core.history.api.HistoryEntry
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class EventGuardService(
    private val service: EventCrudService,
    private val historyService: HistoryCrudService,
    private val accountService: AccountCrudService,
    private val searchService: SearchService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "event.read"
        private const val PERMISSION_WRITE = "event.write"
        private const val PERMISSION_ADMIN = "event.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Event API")

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getInfo(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.getInfo(id, account)
        }

    fun search(auth: Authentication, request: EventSearchRequest, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val system = accountService.getSystemAccount()
            searchService.searchEvents(system, request, pageable)
        }

    fun getLocation(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.getLocation(id) }

    fun getRegistration(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.getRegistration(id) }

    fun getCategories(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.getCategories(id) }

    fun getHistory(auth: Authentication, id: Long, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { historyService.getForEvent(id, pageable) }

    fun getStats(auth: Authentication) = auth.checkPermission(PERMISSION_ADMIN) { service.getStats() }

    fun update(auth: Authentication, id: Long, request: EventChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) {
                service.update(accountService.find(auth), id, request)
            }
        }

    fun delete(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }

    fun setFeatured(auth: Authentication, id: Long, value: PatchRequest<Boolean>) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceAction(auth, "FEATURED", id.toString(), value) {
                service.setFeatured(accountService.find(auth), id, value)
            }
        }

    fun setPublished(auth: Authentication, id: Long, value: PatchRequest<Boolean>) =
        auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceAction(auth, "PUBLISHED", id.toString(), value) {
                service.setPublished(accountService.find(auth), id, value)
            }
        }
}
