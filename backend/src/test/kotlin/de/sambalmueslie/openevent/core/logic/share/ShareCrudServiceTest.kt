package de.sambalmueslie.openevent.core.logic.share

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.ShareChangeListener
import de.sambalmueslie.openevent.core.share.ShareCrudService
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.EventTestData
import de.sambalmueslie.openevent.testdata.ShareTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class ShareCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var eventService: EventCrudService

    @Inject
    lateinit var service: ShareCrudService

    private fun createEvent(actor: Account, shared: Boolean = true): Event =
        EventTestData.create(eventService, actor, EventTestData.createRequest(location = null, shared = shared))

    private fun registerListener(): ShareChangeListener {
        val listener = mockk<ShareChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        every { listener.enabledChanged(any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        // the share is auto-created as part of event creation
        val event = createEvent(actor)
        val share = service.findByEventId(event.id)!!

        assertTrue(share.id.isNotBlank())
        assertEquals(event.id, share.eventId)
        assertTrue(share.enabled)
        verify { listener.handleCreated(actor, share) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val share = service.findByEventId(createEvent(actor).id)!!

        assertEquals(share, service.get(share.id))
        assertNull(service.get("unknown"))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = service.findByEventId(createEvent(actor).id)!!
        val second = service.findByEventId(createEvent(actor).id)!!

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val share = service.findByEventId(createEvent(actor).id)!!
        val listener = registerListener()

        val updated = service.update(actor, share.id, ShareTestData.updateRequest())

        assertEquals(share.id, updated.id)
        assertFalse(updated.enabled)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val share = service.findByEventId(createEvent(actor).id)!!
        val listener = registerListener()

        val deleted = service.delete(actor, share.id)

        assertEquals(share, deleted)
        assertNull(service.get(share.id))
        verify(exactly = 1) { listener.handleDeleted(actor, share) }
    }

    @Test
    fun findByEventAndFindByEventId() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        val share = service.findByEventId(event.id)!!

        assertEquals(share, service.findByEventId(event.id))
        assertNull(service.findByEventId(-1))
    }

    @Test
    fun setEnabledUpdatesExisting() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor, shared = true)
        val share = service.findByEventId(event.id)!!
        val listener = registerListener()

        val result = service.setEnabled(actor, event, PatchRequest(false))

        assertNotNull(result)
        assertFalse(result!!.enabled)
        assertEquals(share.id, result.id)
        // setEnabled on an existing share fires the dedicated enabledChanged, not handleUpdated
        verify(exactly = 1) { listener.enabledChanged(actor, result) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
    }

    @Test
    fun setEnabledCreatesWhenMissing() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        service.deleteByEvent(actor, event)
        val listener = registerListener()

        val result = service.setEnabled(actor, event, PatchRequest(true))

        assertNotNull(result)
        assertEquals(event.id, result!!.eventId)
        assertTrue(result.enabled)
        verify(exactly = 1) { listener.handleCreated(actor, result) }
        verify(exactly = 0) { listener.enabledChanged(any(), any()) }
    }

    @Test
    fun deleteByEvent() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        val share = service.findByEventId(event.id)!!
        val listener = registerListener()

        val deleted = service.deleteByEvent(actor, event)

        assertEquals(share, deleted)
        assertNull(service.findByEventId(event.id))
        verify(exactly = 1) { listener.handleDeleted(actor, share) }
    }

    @Test
    fun getInfo() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        val share = service.findByEventId(event.id)!!

        val info = service.getInfo(event, actor)

        assertNotNull(info)
        assertEquals(share, info!!.share)
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val event = createEvent(actor)
        val share = service.findByEventId(event.id)!!
        verify(exactly = 1) { listener.handleCreated(actor, share) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, share.id, ShareTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, share.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}
