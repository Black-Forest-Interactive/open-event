package de.sambalmueslie.openevent.group


import de.sambalmueslie.openevent.common.auth.checkPermission
import de.sambalmueslie.openevent.group.api.Group
import de.sambalmueslie.openevent.group.api.GroupAPI
import de.sambalmueslie.openevent.group.api.GroupAPI.Companion.PERMISSION_GROUP_READ
import de.sambalmueslie.openevent.group.api.GroupAPI.Companion.PERMISSION_GROUP_WRITE
import de.sambalmueslie.openevent.group.api.GroupChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag
import reactor.core.publisher.Mono


@Controller("/api/group")
@Tag(name = "Group API")
class GroupController(private val service: GroupService) : GroupAPI {


    @Get("/{id}")
    override fun get(auth: Authentication, @PathVariable id: Long): Mono<Group> = auth.checkPermission(PERMISSION_GROUP_READ) {
        Mono.justOrEmpty(service.get(id))
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Mono<Page<Group>> = auth.checkPermission(PERMISSION_GROUP_READ) {
        Mono.just(service.getAll(pageable))
    }

    @Post()
    override fun create(auth: Authentication, @Body request: GroupChangeRequest): Mono<Group> = auth.checkPermission(PERMISSION_GROUP_WRITE) {
        Mono.just(service.create(request))
    }

    @Put("/{id}")
    override fun update(auth: Authentication, @PathVariable id: Long, @Body request: GroupChangeRequest): Mono<Group> = auth.checkPermission(PERMISSION_GROUP_WRITE) {
        Mono.just(service.update(id, request))
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, @PathVariable id: Long): Mono<Group> = auth.checkPermission(PERMISSION_GROUP_WRITE) {
        Mono.justOrEmpty(service.delete(id))
    }
}
