package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.EventAPI
import de.sambalmueslie.openevent.api.EventAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.EventAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.EventAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.auth.getRealmRoles
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.event.EventCrudService
import de.sambalmueslie.openevent.core.model.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/event")
@Tag(name = "Event API")
class EventController(
    private val service: EventCrudService,
    private val accountService: AccountCrudService
) : EventAPI {

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { service.get(id) }
    }

    @Get("/{id}/info")
    override fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { service.getInfo(id) }
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
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) { service.create(request, auth) }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: EventChangeRequest): Event {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) { service.update(id, request) }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Event? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) { service.delete(id) }
    }

    @Put("/{id}/published")
    override fun setPublished(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>): Event? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) { service.setPublished(id, value) }
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

}
