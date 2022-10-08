package de.sambalmueslie.openevent.user


import de.sambalmueslie.openevent.common.auth.checkPermission
import de.sambalmueslie.openevent.user.api.User
import de.sambalmueslie.openevent.user.api.UserChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag
import reactor.core.publisher.Mono

@Controller("/api/user")
@Tag(name = "User API")
class UserController(private val service: UserService) {

    companion object {
        const val PERMISSION_USER_READ = "user.read"
        const val PERMISSION_USER_WRITE = "user.write"
    }

    @Get("/{userId}")
    fun get(auth: Authentication, userId: Long): Mono<User> = auth.checkPermission(PERMISSION_USER_READ) {
        Mono.justOrEmpty(service.get(userId))
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Mono<Page<User>> = auth.checkPermission(PERMISSION_USER_READ) {
        Mono.just(service.getAll(pageable))
    }

    @Post()
    fun create(auth: Authentication, @Body request: UserChangeRequest): Mono<User> = auth.checkPermission(PERMISSION_USER_WRITE) {
        Mono.just(service.create(request))
    }

    @Put("/{userId}")
    fun update(auth: Authentication, userId: Long, @Body request: UserChangeRequest): Mono<User> = auth.checkPermission(PERMISSION_USER_WRITE) {
        Mono.just(service.update(userId, request))
    }

    @Delete("/{userId}")
    fun delete(auth: Authentication, userId: Long): Mono<User> = auth.checkPermission(PERMISSION_USER_WRITE) {
        Mono.justOrEmpty(service.delete(userId))
    }


}
