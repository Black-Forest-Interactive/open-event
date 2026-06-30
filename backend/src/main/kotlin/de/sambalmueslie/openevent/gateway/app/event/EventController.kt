package de.sambalmueslie.openevent.gateway.app.event

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.event.api.EventUpdateTextRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse
import io.micronaut.data.model.Pageable
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.server.types.files.SystemFile
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
    fun get(auth: Authentication, id: Long) {
        return service.get(auth, id)
    }

    @Get("/{id}/info")
    fun getInfo(auth: Authentication, id: Long): EventInfo? {
        return service.getInfo(auth, id)
    }

    @Post()
    fun create(auth: Authentication, @Body request: EventChangeRequest) = service.create(auth, request)

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: EventChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long) = service.delete(auth, id)

    @Put("/{id}/published")
    fun setPublished(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>) = service.setPublished(auth, id, value)


    @Put("/{id}/shared")
    fun setShared(auth: Authentication, id: Long, @Body value: PatchRequest<Boolean>): EventInfo? {
        return service.setShared(auth, id, value)
    }

    @Put("/{id}/bookmark")
    fun setBookmarked(auth: Authentication, id: Long): EventInfo? {
        return service.setBookmarked(auth, id)
    }

    @Delete("/{id}/bookmark")
    fun clearBookmarked(auth: Authentication, id: Long): EventInfo? {
        return service.clearBookmarked(auth, id)
    }

    @Put("/{id}/title")
    fun setTitle(auth: Authentication, id: Long, @Body value: PatchRequest<String>) =
        service.setTitle(auth, id, value)

    @Put("/{id}/shortText")
    fun setShortText(auth: Authentication, id: Long, @Body value: PatchRequest<String>) =
        service.setShortText(auth, id, value)

    @Put("/{id}/longText")
    fun setLongText(auth: Authentication, id: Long, @Body value: PatchRequest<String>) =
        service.setLongText(auth, id, value)

    @Put("/{id}/tags")
    fun setTags(auth: Authentication, id: Long, @Body value: PatchRequest<Set<String>>) =
        service.setTags(auth, id, value)

    @Put("/{id}/text")
    fun setText(auth: Authentication, id: Long, @Body request: EventUpdateTextRequest) =
        service.setText(auth, id, request)

    @Put("/{id}/categories")
    fun setCategories(auth: Authentication, id: Long, @Body categoryIds: PatchRequest<Set<Long>>) = service.setCategories(auth, id, categoryIds)

    @Put("/{id}/audiences")
    fun setAudiences(auth: Authentication, id: Long, @Body audienceIds: PatchRequest<Set<Long>>) = service.setAudiences(auth, id, audienceIds)

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Get("/event/{eventId}/pdf")
    fun export(auth: Authentication, eventId: Long): SystemFile? = service.export(auth, eventId)
}