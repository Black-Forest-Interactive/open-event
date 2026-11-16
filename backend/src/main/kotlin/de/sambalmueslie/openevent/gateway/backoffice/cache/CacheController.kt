package de.sambalmueslie.openevent.gateway.backoffice.cache

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Delete
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/cache")
@Tag(name = "BACKOFFICE Cache API")
class CacheController(private val service: CacheGuardService) {

    @Get("/{key}")
    fun get(auth: Authentication, key: String) = service.get(auth, key)

    @Get()
    fun getAll(auth: Authentication) = service.getAll(auth)

    @Delete("/{key}")
    fun reset(auth: Authentication, key: String) = service.reset(auth, key)

    @Delete()
    fun resetAll(auth: Authentication) = service.resetAll(auth)
}
