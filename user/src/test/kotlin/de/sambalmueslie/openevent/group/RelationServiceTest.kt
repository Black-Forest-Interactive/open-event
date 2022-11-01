package de.sambalmueslie.openevent.group

import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.group.api.GroupChangeRequest
import de.sambalmueslie.openevent.user.UserService
import de.sambalmueslie.openevent.user.api.UserChangeRequest
import de.sambalmueslie.openevent.user.api.UserType
import io.micronaut.data.model.Pageable
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.every
import io.mockk.mockk
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@MicronautTest
internal class RelationServiceTest {
    @Inject
    lateinit var userService: UserService

    @Inject
    lateinit var groupService: GroupService

    @Inject
    lateinit var service: RelationService

    private val timeProvider = mockk<TimeProvider>()

    @MockBean(TimeProvider::class)
    fun timeProvider() = timeProvider


    @Test
    fun checkCrud() {
        val now = LocalDateTime.of(2022, 10, 8, 20, 15, 0)
        every { timeProvider.now() } returns now

        val group1 = groupService.create(GroupChangeRequest("group-1"))
        val group2 = groupService.create(GroupChangeRequest("group-2"))
        val user1 = userService.create(UserChangeRequest("user-1", UserType.IDP, "userName", "firstName", "lastName", "email-1", "mobile", "phone"))
        val user2 = userService.create(UserChangeRequest("user-2", UserType.IDP, "userName", "firstName", "lastName", "email-2", "mobile", "phone"))

        var result = service.assignUserToGroup(user1.id, group1.id)
        assertEquals(listOf(group1), result.content)

        result = service.assignUserToGroup(user2.id, group1.id)
        assertEquals(listOf(group1), result.content)

        result = service.assignUserToGroup(user1.id, group2.id)
        assertEquals(listOf(group1, group2), result.content)

        result = service.getUserGroups(user1.id, Pageable.from(0))
        assertEquals(listOf(group1, group2), result.content)

        assertEquals(listOf(user1, user2), service.getGroupMember(group1.id, Pageable.from(0)).content)

        service.revokeUserFromGroup(user2.id, group1.id)
        assertEquals(listOf(user1), service.getGroupMember(group1.id, Pageable.from(0)).content)
    }
}
