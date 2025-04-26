package de.sambalmueslie.openevent.gateway.app.event

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/event")
@Tag(name = "APP Event API")
class EventController(private val service: EventGuardService) {


    @Post("search")
    fun search(auth: Authentication, @Body request: EventSearchRequest, pageable: Pageable): EventSearchResponse {
        return service.search(auth, request, pageable)
    }

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Event? {
        return service.get(auth, id)
    }

    @Get("/{id}/info")
    fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return service.getInfo(auth, id)
    }

    @Post()
    fun create(auth: Authentication, @Body request: EventChangeRequest): Event {
        return service.create(auth, request)
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: EventChangeRequest): Event {
        return service.update(auth, id, request)
    }

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long): Event? {
        return service.delete(auth, id)
    }

    @Put("/{id}/published")
    fun setPublished(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>): Event? {
        return service.setPublished(auth, id, value)
    }
}