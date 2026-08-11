package de.sambalmueslie.openevent.gateway.backoffice.event

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/event")
@Tag(name = "BACKOFFICE Event API")
class EventController(private val service: EventGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get("/{id}/info")
    fun getInfo(auth: Authentication, id: Long) = service.getInfo(auth, id)

    @Post("/search")
    fun search(auth: Authentication, @Body request: EventSearchRequest, pageable: Pageable): EventSearchResponse = service.search(auth, request, pageable)

    @Get("/{id}/location")
    fun getLocation(auth: Authentication, id: Long) = service.getLocation(auth, id)

    @Get("/{id}/registration")
    fun getRegistration(auth: Authentication, id: Long) = service.getRegistration(auth, id)

    @Get("/{id}/category")
    fun getCategories(auth: Authentication, id: Long) = service.getCategories(auth, id)

    @Get("/{id}/history")
    fun getHistory(auth: Authentication, id: Long, pageable: Pageable) = service.getHistory(auth, id, pageable)

    @Get("/stats")
    fun getStats(auth: Authentication) = service.getStats(auth)

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: EventChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long) = service.delete(auth, id)

    @Put("/{id}/featured")
    fun setFeatured(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>) = service.setFeatured(auth, id, value)

    @Put("/{id}/status")
    fun setStatus(auth: Authentication, id: Long, @Body value: EventStatus) = service.setStatus(auth, id, value)

    @Put("/{id}/published")
    fun setPublished(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>) = service.setPublished(auth, id, value)
}
