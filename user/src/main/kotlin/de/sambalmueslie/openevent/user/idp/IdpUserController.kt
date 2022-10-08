package de.sambalmueslie.openevent.user.idp

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag
import reactor.core.publisher.Mono

@Controller("/api/user/idp")
@Tag(name = "IDP User API")
class IdpUserController(private val service: IdpUserService) {

    @Get()
    fun get(auth: Authentication) = Mono.justOrEmpty(service.get(auth))

}
