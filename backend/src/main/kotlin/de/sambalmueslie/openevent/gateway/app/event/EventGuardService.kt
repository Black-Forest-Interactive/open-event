package de.sambalmueslie.openevent.gateway.app.event

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.event.api.EventUpdateTextRequest
import de.sambalmueslie.openevent.core.export.ExportService
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse
import de.sambalmueslie.openevent.error.IllegalAccessException
import de.sambalmueslie.openevent.infrastructure.metrics.MetricsService
import io.micronaut.data.model.Pageable
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class EventGuardService(
    private val service: EventCrudService,
    private val exportService: ExportService,
    private val searchService: SearchService,
    private val accountService: AccountCrudService,
    metrics: MetricsService
) {

    companion object {
        private const val PERMISSION_READ = "event.read"
        private const val PERMISSION_WRITE = "event.write"
    }

    private val probe = metrics.getProbe("APP Event API", Event::class)

    fun search(auth: Authentication, request: EventSearchRequest, pageable: Pageable): EventSearchResponse {
        return auth.checkPermission(PERMISSION_READ) {
            searchService.searchEvents(accountService.find(auth), request, pageable)
        }
    }

    fun get(auth: Authentication, id: Long) {
        return auth.checkPermission(PERMISSION_READ) {
            probe.traceAccess(auth, id) { service.get(id) }
        }
    }

    fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission null
            probe.traceAccess(auth, id) { service.getInfo(id, account) }
        }
    }

    fun create(auth: Authentication, request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE) {
            probe.traceCreate(auth, request) {
                service.create(accountService.find(auth), request)
            }
        }
    }

    fun update(auth: Authentication, id: Long, request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission create(auth, request)
            probe.traceUpdate(auth, request) {
                service.update(account, event.id, request)
            }
        }
    }

    fun delete(auth: Authentication, id: Long) : Event?{
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceDelete(auth) {
                service.delete(account, event.id)
            }
        }
    }

    fun setPublished(auth: Authentication, id: Long, value: PatchRequest<Boolean>): Event? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "PUBLISHED", id.toString(), value) {
                service.setPublished(account, event.id, value)
            }
        }
    }

    fun setShared(auth: Authentication, id: Long, value: PatchRequest<Boolean>): EventInfo? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "PUBLISHED", id.toString(), value) {
                service.setShared(account, event.id, value)
            }
        }
    }

    fun setBookmarked(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.find(auth)
            val event = service.get(id) ?: return@checkPermission null
            probe.traceAction(auth, "BOOKMARK", id.toString()) { service.setBookmarked(account, event) }
            service.getInfo(id, account)
        }
    }

    fun clearBookmarked(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.find(auth)
            val event = service.get(id) ?: return@checkPermission null
            probe.traceAction(auth, "UNBOOKMARK", id.toString()) { service.clearBookmarked(account, event) }
            service.getInfo(id, account)
        }
    }

    fun setTitle(auth: Authentication, id: Long, value: PatchRequest<String>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "TITLE", id.toString(), value) { service.setTitle(account, event.id, value) }
        }

    fun setShortText(auth: Authentication, id: Long, value: PatchRequest<String>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "SHORT_TEXT", id.toString(), value) { service.setShortText(account, event.id, value) }
        }

    fun setLongText(auth: Authentication, id: Long, value: PatchRequest<String>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "LONG_TEXT", id.toString(), value) { service.setLongText(account, event.id, value) }
        }

    fun setTags(auth: Authentication, id: Long, value: PatchRequest<Set<String>>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "TAGS", id.toString(), value) { service.setTags(account, event.id, value) }
        }

    fun setText(auth: Authentication, id: Long, request: EventUpdateTextRequest) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "TEXT", id.toString(), request) { service.setText(account, event.id, request) }
        }

    fun setCategories(auth: Authentication, id: Long, categoryIds: PatchRequest<Set<Long>>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "CATEGORIES", id.toString(), categoryIds) { service.setCategories(account, event.id, categoryIds) }
        }

    fun setAudiences(auth: Authentication, id: Long, audienceIds: PatchRequest<Set<Long>>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, "AUDIENCES", id.toString(), audienceIds) { service.setAudiences(account, event.id, audienceIds) }
        }


    fun getIfAccessible(auth: Authentication, id: Long): Pair<Event, Account>? {
        val event = service.get(id) ?: return null
        val account = accountService.find(auth)
        if (event.owner.id != account.id) throw IllegalAccessException("You are not allowed to change that event")
        return Pair(event, account)
    }


    fun export(auth: Authentication, eventId: Long): SystemFile? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, eventId) ?: return@checkPermission null
            exportService.exportEventPdf(event.id, account)
        }
    }
}