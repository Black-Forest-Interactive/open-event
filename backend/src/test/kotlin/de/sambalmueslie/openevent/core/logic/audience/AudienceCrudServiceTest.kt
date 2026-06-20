package de.sambalmueslie.openevent.core.logic.audience

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.audience.AudienceChangeListener
import de.sambalmueslie.openevent.core.audience.AudienceCrudService
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.AudienceTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class AudienceCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: AudienceCrudService

    private fun registerListener(): AudienceChangeListener {
        val listener = mockk<AudienceChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val request = AudienceTestData.createRequest()
        val audience = service.create(actor, request)

        assertTrue(audience.id > 0)
        assertEquals(request.name, audience.name)
        assertEquals(request.iconUrl, audience.iconUrl)
        verify { listener.handleCreated(actor, audience) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val audience = AudienceTestData.create(service, actor)

        assertEquals(audience, service.get(audience.id))
        assertEquals(listOf(audience), service.getByIds(setOf(audience.id)))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = AudienceTestData.create(service, actor, AudienceTestData.createRequest("first"))
        val second = AudienceTestData.create(service, actor, AudienceTestData.createRequest("second"))

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val audience = AudienceTestData.create(service, actor)
        val listener = registerListener()

        val request = AudienceTestData.updateRequest()
        val updated = service.update(actor, audience.id, request)

        assertEquals(audience.id, updated.id)
        assertEquals(request.name, updated.name)
        assertEquals(request.iconUrl, updated.iconUrl)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val audience = AudienceTestData.create(service, actor)
        val listener = registerListener()

        val deleted = service.delete(actor, audience.id)

        assertEquals(audience, deleted)
        assertNull(service.get(audience.id))
        assertEquals(emptyList<Audience>(), service.getAll(Pageable.from(0)).content)
        verify(exactly = 1) { listener.handleDeleted(actor, audience) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun findByName() {
        val actor = AccountTestData.createActor(accountStorage)
        val request = AudienceTestData.createRequest()
        val audience = AudienceTestData.create(service, actor, request)

        assertEquals(audience, service.findByName(request.name))
        assertNull(service.findByName("unknown"))
    }

    @Test
    fun isValidRejectsBlankNameOnCreate() {
        val actor = AccountTestData.createActor(accountStorage)

        assertThrows(InvalidRequestException::class.java) {
            service.create(actor, AudienceTestData.createRequest(name = " "))
        }
    }

    @Test
    fun isValidRejectsBlankNameOnUpdate() {
        val actor = AccountTestData.createActor(accountStorage)
        val audience = AudienceTestData.create(service, actor)

        assertThrows(InvalidRequestException::class.java) {
            service.update(actor, audience.id, AudienceTestData.updateRequest(name = " "))
        }
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val audience = service.create(actor, AudienceTestData.createRequest())
        verify(exactly = 1) { listener.handleCreated(actor, audience) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, audience.id, AudienceTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, audience.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}
