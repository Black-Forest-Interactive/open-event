package de.sambalmueslie.openevent.gateway.app.event

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse
import de.sambalmueslie.openevent.error.IllegalAccessException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class EventGuardService(
    private val service: EventCrudService, private val searchService: SearchService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {

    companion object {
        private const val PERMISSION_READ = "event.read"
        private const val PERMISSION_WRITE = "event.write"
    }

    private val logger = audit.getLogger("APP Event API")

    fun search(auth: Authentication, request: EventSearchRequest, pageable: Pageable): EventSearchResponse {
        return auth.checkPermission(PERMISSION_READ) { searchService.searchEvents(accountService.find(auth), request, pageable) }
    }

    fun get(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_READ) {
            service.get(id)
        }
    }

    fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.getInfo(id, account)
        }
    }

    fun create(auth: Authentication, request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }


    fun update(auth: Authentication, id: Long, request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission create(auth, request)
            logger.traceUpdate(auth, request) { service.update(account, event.id, request) }
        }
    }

    fun delete(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            logger.traceDelete(auth) { service.delete(account, event.id) }
        }
    }

    fun setPublished(auth: Authentication, id: Long, value: PatchRequest<Boolean>): Event? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, "PUBLISHED", id.toString(), value) {
                service.setPublished(account, event.id, value)
            }
        }
    }


    fun getIfAccessible(auth: Authentication, id: Long): Pair<Event, Account>? {
        val event = service.get(id) ?: return null
        val account = accountService.find(auth)
        if (event.owner.id != account.id) throw IllegalAccessException("You are not allowed to change that event")
        return Pair(event, account)
    }


}