package de.sambalmueslie.openevent.group

import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.group.api.Group
import de.sambalmueslie.openevent.group.api.GroupChangeListener
import de.sambalmueslie.openevent.group.api.GroupChangeRequest
import de.sambalmueslie.openevent.user.api.*
import io.micronaut.data.model.Pageable
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

@MicronautTest
internal class GroupServiceTest {


    @Inject
    lateinit var service: GroupService

    private val timeProvider = mockk<TimeProvider>()

    @MockBean(TimeProvider::class)
    fun timeProvider() = timeProvider

    private val listener = mockk<GroupChangeListener>()

    @Test
    fun checkCrud() {
        val now = LocalDateTime.of(2022, 10, 8, 20, 15, 0)
        every { timeProvider.now() } returns now

        service.register(listener)

        // create
        val createRequest = GroupChangeRequest("name")
        var result = service.create(createRequest)

        var reference = Group(1, createRequest.name)
        assertEquals(reference, result)
        verify { listener.handleCreated(reference) }
        verify { listener.hashCode() }
        confirmVerified(listener)

        // read
        assertEquals(reference, service.get(reference.id))
        assertEquals(listOf(reference), service.getAll(Pageable.from(0)).content)

        // update
        val updateRequest = GroupChangeRequest("update-name")
        result = service.update(reference.id, updateRequest)

        reference = Group(1, updateRequest.name)
        assertEquals(reference, result)
        verify { listener.handleUpdated(reference) }
        verify { listener.hashCode() }
        confirmVerified(listener)

        // delete
        service.delete(reference.id)

        // read empty
        assertEquals(null, service.get(reference.id))
        assertEquals(emptyList<Group>(), service.getAll(Pageable.from(0)).content)
    }

}
