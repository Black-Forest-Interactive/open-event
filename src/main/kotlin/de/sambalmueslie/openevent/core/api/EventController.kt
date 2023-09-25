package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.CategoryAPI
import de.sambalmueslie.openevent.api.EventAPI
import de.sambalmueslie.openevent.api.EventAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.EventAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.EventAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.auth.getRealmRoles
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.event.EventCrudService
import de.sambalmueslie.openevent.core.logic.event.EventSearchService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/event")
@Tag(name = "Event API")
class EventController(
    private val service: EventCrudService,
    private val search: EventSearchService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : EventAPI {
    private val logger = audit.getLogger("Event API")

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { service.get(id) }
    }

    @Get("/{id}/info")
    override fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.getInfo(id, account)
        }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Event> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.getAll(pageable)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission Page.empty()
                service.getAllForAccount(account, pageable)
            }
        }
    }

    @Get("/info")
    override fun getInfos(auth: Authentication, pageable: Pageable): Page<EventInfo> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.getInfos(pageable)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission Page.empty()
                service.getInfosForAccount(account, pageable)
            }

        }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Post("/{ownerId}")
    override fun create(auth: Authentication, ownerId: Long, @Body request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_ADMIN) {
            logger.traceCreate(auth, request) {
                val owner = accountService.get(ownerId)
                require(owner != null) { "Cannot find account for user" }
                service.create(owner, request)
            }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceUpdate(auth, request) {
                service.update(accountService.find(auth), id, request)
            }
        }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

    @Put("/{id}/published")
    override fun setPublished(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>): Event? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) {
            logger.traceAction(auth, "PUBLISHED", id.toString(), value) {
                service.setPublished(accountService.find(auth), id, value)
            }
        }
    }

    @Get("/{id}/location")
    override fun getLocation(auth: Authentication, id: Long): Location? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { service.getLocation(id) }
    }


    @Get("/{id}/registration")
    override fun getRegistration(auth: Authentication, id: Long): Registration? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { service.getRegistration(id) }
    }

    @Get("/{id}/category")
    override fun getCategories(auth: Authentication, id: Long): List<Category> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { service.getCategories(id) }
    }

    private fun isAdmin(auth: Authentication) = auth.getRealmRoles().contains(PERMISSION_ADMIN)

    @Get("/search")
    override fun search(auth: Authentication, @QueryValue query: String, pageable: Pageable): Page<EventInfo> {
        return auth.checkPermission(PERMISSION_READ) { search.searchInfo(query, pageable) }
    }

    @Post("/search")
    fun buildIndex(auth: Authentication) {
        return auth.checkPermission(PERMISSION_ADMIN) {
            search.createIndex()
            HttpResponse.created("")
        }
    }

    @Get("/stats")
    override fun getStats(auth: Authentication): List<EventStats> {
        return auth.checkPermission(CategoryAPI.PERMISSION_ADMIN) { service.getStats() }
    }
}
