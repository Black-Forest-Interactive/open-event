package de.sambalmueslie.openevent.gateway.portal.audience

import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/portal/audience")
@Tag(name = "APP Audience API")
class AudienceController(private val service: AudienceGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)
}
