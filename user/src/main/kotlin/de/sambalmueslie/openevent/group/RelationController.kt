package de.sambalmueslie.openevent.group


import de.sambalmueslie.openevent.common.auth.checkPermission
import de.sambalmueslie.openevent.group.api.Group
import de.sambalmueslie.openevent.user.api.User
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag
import reactor.core.publisher.Mono

@Controller("/api/relation")
@Tag(name = "Group Member API")
class RelationController(private val service: RelationService) {

    companion object {
        const val PERMISSION_RELATION_READ = "relation.read"
        const val PERMISSION_RELATION_WRITE = "relation.write"
    }

    @Get("/group/{groupId}/member")
    fun getGroupMember(auth: Authentication, @PathVariable groupId: Long, pageable: Pageable): Mono<Page<User>> = auth.checkPermission(PERMISSION_RELATION_READ) {
        Mono.just(service.getGroupMember(groupId, pageable))
    }

    @Get("/user/{userId}/groups")
    fun getUserGroups(auth: Authentication, @PathVariable userId: Long, pageable: Pageable): Mono<Page<Group>> = auth.checkPermission(PERMISSION_RELATION_READ) {
        Mono.just(service.getUserGroups(userId, pageable))
    }

    @Put("/user/{userId}/group/{groupId}")
    fun assignUserToGroup(auth: Authentication, @PathVariable userId: Long, @PathVariable groupId: Long): Mono<Page<Group>> = auth.checkPermission(PERMISSION_RELATION_WRITE) {
        Mono.just(service.assignUserToGroup(userId, groupId))
    }

    @Delete("/user/{userId}/group/{groupId}")
    fun revokeUserFromGroup(auth: Authentication, @PathVariable userId: Long, @PathVariable groupId: Long): Mono<Page<Group>> = auth.checkPermission(PERMISSION_RELATION_WRITE) {
        Mono.just(service.revokeUserFromGroup(userId, groupId))
    }
}
