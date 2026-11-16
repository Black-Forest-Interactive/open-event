package de.sambalmueslie.openevent.gateway.backoffice.audience

import de.sambalmueslie.openevent.core.audience.api.AudienceChangeRequest
import de.sambalmueslie.openevent.core.search.api.AudienceSearchRequest
import de.sambalmueslie.openevent.core.search.api.AudienceSearchResponse
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/audience")
@Tag(name = "BACKOFFICE Audience API")
class AudienceController(private val service: AudienceGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get("/find/by/name")
    fun findByName(auth: Authentication, @QueryValue name: String) = service.findByName(auth, name)

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)

    @Post("search")
    fun searchAudiences(auth: Authentication, @Body request: AudienceSearchRequest, pageable: Pageable): AudienceSearchResponse = service.searchAudiences(auth, request, pageable)

    @Post()
    fun create(auth: Authentication, @Body request: AudienceChangeRequest) = service.create(auth, request)

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AudienceChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long) = service.delete(auth, id)
}
