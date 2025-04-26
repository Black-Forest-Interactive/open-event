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
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/event")
@Tag(name = "BACKOFFICE Event API")
class EventController(
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


    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.get(id)
        }
    }

    @Get("/{id}/info")
    fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.getInfo(id, account)
        }
    }

    @Post("/search")
    fun search(auth: Authentication, @Body request: EventSearchRequest, pageable: Pageable): EventSearchResponse {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val system = accountService.getSystemAccount()
            searchService.searchEvents(system, request, pageable)
        }
    }

    @Get("/{id}/location")
    fun getLocation(auth: Authentication, id: Long): Location? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getLocation(id) }
    }


    @Get("/{id}/registration")
    fun getRegistration(auth: Authentication, id: Long): Registration? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getRegistration(id) }
    }

    @Get("/{id}/category")
    fun getCategories(auth: Authentication, id: Long): List<Category> {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getCategories(id) }
    }

    @Get("/{id}/history")
    fun getHistory(auth: Authentication, id: Long, pageable: Pageable): Page<HistoryEntry> {
        return auth.checkPermission(PERMISSION_ADMIN) { historyService.getForEvent(id, pageable) }
    }

    @Get("/stats")
    fun getStats(auth: Authentication): List<EventStats> {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getStats() }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) {
                service.update(accountService.find(auth), id, request)
            }
        }
    }

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

    @Put("/{id}/published")
    fun setPublished(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>): Event? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceAction(auth, "PUBLISHED", id.toString(), value) {
                service.setPublished(accountService.find(auth), id, value)
            }
        }
    }

}