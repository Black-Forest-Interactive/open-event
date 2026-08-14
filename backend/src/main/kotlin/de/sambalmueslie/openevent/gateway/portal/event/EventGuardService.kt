package de.sambalmueslie.openevent.gateway.portal.event

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.*
import de.sambalmueslie.openevent.core.export.ExportService
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse
import de.sambalmueslie.openevent.error.IllegalAccessException
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditAction
import de.sambalmueslie.openevent.infrastructure.metrics.MetricsService
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsSource
import io.micronaut.data.model.Page
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

    private val probe = metrics.getProbe(MetricsSource.PORTAL, "APP Event API", Event::class)

    fun search(auth: Authentication, request: EventSearchRequest, pageable: Pageable): EventSearchResponse {
        return auth.checkPermission(PERMISSION_READ) {
            searchService.searchEvents(accountService.find(auth), request, pageable)
        }
    }

    fun get(auth: Authentication, id: Long): Event? =
        auth.checkPermission(PERMISSION_READ) {
            probe.traceAccess(auth, id) { service.get(id) }
        }

    fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission null
            getReadable(auth, id) ?: return@checkPermission null
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

    fun delete(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceDelete(auth) {
                service.delete(account, event.id)
            }
        }
    }

    fun cancel(auth: Authentication, id: Long, request: EventCancelRequest): Event? =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_CANCELLED, id.toString(), request) { service.cancel(account, event.id, request.reason) }
        }

    fun setStatus(auth: Authentication, id: Long, value: EventStatus): Event? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_STATUS_CHANGED, id.toString(), value) {
                service.setStatus(account, event.id, value)
            }
        }
    }

    fun setPublished(auth: Authentication, id: Long, value: PatchRequest<Boolean>): Event? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_PUBLISH_CHANGED, id.toString(), value) {
                service.setPublished(account, event.id, value)
            }
        }
    }

    fun setShared(auth: Authentication, id: Long, value: PatchRequest<Boolean>): EventInfo? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_SHARED_CHANGED, id.toString(), value) {
                service.setShared(account, event.id, value)
            }
        }
    }

    fun setBookmarked(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.find(auth)
            val event = service.get(id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_BOOKMARK_ADDED, id.toString()) { service.setBookmarked(account, event) }
            service.getInfo(id, account)
        }
    }

    fun clearBookmarked(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.find(auth)
            val event = service.get(id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_BOOKMARK_REMOVED, id.toString()) { service.clearBookmarked(account, event) }
            service.getInfo(id, account)
        }
    }

    fun setTitle(auth: Authentication, id: Long, value: PatchRequest<String>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_TITLE_CHANGED, id.toString(), value) { service.setTitle(account, event.id, value) }
        }

    fun setShortText(auth: Authentication, id: Long, value: PatchRequest<String>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_SHORT_TEXT_CHANGED, id.toString(), value) { service.setShortText(account, event.id, value) }
        }

    fun setLongText(auth: Authentication, id: Long, value: PatchRequest<String>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_LONG_TEXT_CHANGED, id.toString(), value) { service.setLongText(account, event.id, value) }
        }

    fun setTags(auth: Authentication, id: Long, value: PatchRequest<Set<String>>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_TAGS_CHANGED, id.toString(), value) { service.setTags(account, event.id, value) }
        }

    fun setText(auth: Authentication, id: Long, request: EventUpdateTextRequest) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_TEXT_CHANGED, id.toString(), request) { service.setText(account, event.id, request) }
        }

    fun setCategories(auth: Authentication, id: Long, categoryIds: PatchRequest<Set<Long>>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_CATEGORIES_CHANGED, id.toString(), categoryIds) { service.setCategories(account, event.id, categoryIds) }
        }

    fun setAudiences(auth: Authentication, id: Long, audienceIds: PatchRequest<Set<Long>>) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_AUDIENCES_CHANGED, id.toString(), audienceIds) { service.setAudiences(account, event.id, audienceIds) }
        }


    fun getIfAccessible(auth: Authentication, id: Long): Pair<Event, Account>? {
        val event = service.get(id) ?: return null
        val account = accountService.find(auth)
        if (event.owner.id != account.id) throw IllegalAccessException("You are not allowed to change that event")
        return Pair(event, account)
    }

    fun getReadable(auth: Authentication?, id: Long): Event? {
        val event = service.get(id) ?: return null
        if (event.published) return event
        val account = auth?.let { accountService.find(it) } ?: return null
        return if (event.owner.id == account.id) event else null
    }

    fun export(auth: Authentication, id: Long): SystemFile? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            exportService.exportEventPdf(event.id, account)
        }
    }

    fun getAnnouncements(auth: Authentication, id: Long, pageable: Pageable): Page<Announcement> =
        auth.checkPermission(PERMISSION_READ) {
            val event = service.get(id) ?: return@checkPermission Page.empty()
            val account = accountService.find(auth)
            val canAccessAnnouncements = event.owner.id == account.id || service.getInfo(event, account).registration?.participants?.any { it.author.id == account.id } ?: false
            if (!canAccessAnnouncements) return@checkPermission Page.empty()
            service.getAnnouncements(event.id, pageable)
        }

    fun createAnnouncement(auth: Authentication, id: Long, request: AnnouncementChangeRequest) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_ANNOUNCEMENT_ADDED, event.id.toString(), request) {
                service.addAnnouncement(account, event.id, request)
            }
        }

    fun deleteAnnouncement(auth: Authentication, id: Long, announcementId: Long) =
        auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            probe.traceAction(auth, AuditAction.EVENT_ANNOUNCEMENT_REMOVED, id.toString(), announcementId) {
                service.removeAnnouncement(account, event.id, announcementId)
            }
        }
}