package de.sambalmueslie.openevent.user


import de.sambalmueslie.openevent.common.auth.checkPermission
import de.sambalmueslie.openevent.user.api.User
import de.sambalmueslie.openevent.user.api.UserAPI
import de.sambalmueslie.openevent.user.api.UserAPI.Companion.PERMISSION_USER_READ
import de.sambalmueslie.openevent.user.api.UserAPI.Companion.PERMISSION_USER_WRITE
import de.sambalmueslie.openevent.user.api.UserChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag
import reactor.core.publisher.Mono

@Controller("/api/user")
@Tag(name = "User API")
class UserController(private val service: UserService) : UserAPI {

    @Get("/{id}")
    override fun get(auth: Authentication, @PathVariable id: Long): Mono<User> = auth.checkPermission(PERMISSION_USER_READ) {
        Mono.justOrEmpty(service.get(id))
    }
    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Mono<Page<User>> = auth.checkPermission(PERMISSION_USER_READ) {
        Mono.just(service.getAll(pageable))
    }
    @Post()
    override fun create(auth: Authentication, @Body request: UserChangeRequest): Mono<User> = auth.checkPermission(PERMISSION_USER_WRITE) {
        Mono.just(service.create(request))
    }
    @Put("/{id}")
    override fun update(auth: Authentication, @PathVariable id: Long, @Body request: UserChangeRequest): Mono<User> = auth.checkPermission(PERMISSION_USER_WRITE) {
        Mono.just(service.update(id, request))
    }
    @Delete("/{id}")
    override fun delete(auth: Authentication, @PathVariable id: Long): Mono<User> = auth.checkPermission(PERMISSION_USER_WRITE) {
        Mono.justOrEmpty(service.delete(id))
    }


}
