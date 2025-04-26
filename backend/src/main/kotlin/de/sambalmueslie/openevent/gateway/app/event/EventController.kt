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
import de.sambalmueslie.openevent.error.InsufficientPermissionsException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/event")
@Tag(name = "APP Event API")
class EventController(
    private val service: EventCrudService,
    private val searchService: SearchService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "event.read"
        private const val PERMISSION_WRITE = "event.write"
    }


    private val logger = audit.getLogger("APP Event API")

    @Post("search")
    fun search(auth: Authentication, @Body request: EventSearchRequest, pageable: Pageable): EventSearchResponse {
        return auth.checkPermission(PERMISSION_READ) { searchService.searchEvents(accountService.find(auth), request, pageable) }
    }

    @Get("/{id}/info")
    fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.getInfo(id, account)
        }
    }

    @Post()
    fun create(auth: Authentication, @Body request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission create(auth, request)
            logger.traceUpdate(auth, request) { service.update(account, event.id, request) }
        }
    }

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            logger.traceDelete(auth) { service.delete(account, event.id) }
        }
    }

    @Put("/{id}/published")
    fun setPublished(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>): Event? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, account) = getIfAccessible(auth, id) ?: return@checkPermission null
            logger.traceAction(auth, "PUBLISHED", id.toString(), value) {
                service.setPublished(account, event.id, value)
            }
        }
    }


    private fun getIfAccessible(auth: Authentication, id: Long): Pair<Event, Account>? {
        val event = service.get(id) ?: return null
        val account = accountService.find(auth)
        if (event.owner.id != account.id) throw InsufficientPermissionsException("You are not allowed to change that event")
        return Pair(event, account)
    }
}