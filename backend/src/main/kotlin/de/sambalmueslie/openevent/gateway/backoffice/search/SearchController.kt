package de.sambalmueslie.openevent.gateway.backoffice.search

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/search")
@Tag(name = "BACKOFFICE Search API")
class SearchController(private val service: SearchGuardService) {

    @Get()
    fun getInfo(auth: Authentication) = service.getInfo(auth)

    @Get("/{key}")
    fun getInfo(auth: Authentication, key: String) = service.getInfo(auth, key)

    @Post("/{key}")
    fun setup(auth: Authentication, key: String) = service.setup(auth, key)
}
