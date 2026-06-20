package de.sambalmueslie.openevent.core.logic.link

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.link.LinkChangeListener
import de.sambalmueslie.openevent.core.link.LinkCrudService
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.LinkTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class LinkCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: LinkCrudService

    private fun registerListener(): LinkChangeListener {
        val listener = mockk<LinkChangeListener>()
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

        val request = LinkTestData.createRequest()
        val link = service.create(actor, "my-key", request)

        assertTrue(link.id.isNotBlank())
        assertEquals("my-key", link.key)
        assertEquals(request.enabled, link.enabled)
        assertEquals(request.params, link.params)
        verify { listener.handleCreated(actor, link) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val link = LinkTestData.create(service, actor)

        assertEquals(link, service.get(link.id))
        assertNull(service.get("unknown"))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = LinkTestData.create(service, actor, "key-1")
        val second = LinkTestData.create(service, actor, "key-2")

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val link = LinkTestData.create(service, actor)
        val listener = registerListener()

        val request = LinkTestData.updateRequest()
        val updated = service.update(actor, link.id, request)

        assertEquals(link.id, updated.id)
        assertEquals(request.enabled, updated.enabled)
        assertEquals(request.params, updated.params)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val link = LinkTestData.create(service, actor)
        val listener = registerListener()

        val deleted = service.delete(actor, link.id)

        assertEquals(link, deleted)
        assertNull(service.get(link.id))
        verify(exactly = 1) { listener.handleDeleted(actor, link) }
    }

    @Test
    fun findByKey() {
        val actor = AccountTestData.createActor(accountStorage)
        val link = LinkTestData.create(service, actor, "the-key")

        assertEquals(link, service.findByKey("the-key"))
        assertNull(service.findByKey("unknown"))
    }

    @Test
    fun findByKeys() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = LinkTestData.create(service, actor, "key-1")
        val second = LinkTestData.create(service, actor, "key-2")

        assertEquals(setOf(first, second), service.findByKeys(setOf("key-1", "key-2")).toSet())
    }

    @Test
    fun setEnabledUpdatesExisting() {
        val actor = AccountTestData.createActor(accountStorage)
        val link = LinkTestData.create(service, actor, "the-key", LinkTestData.createRequest(enabled = true))
        val listener = registerListener()

        val result = service.setEnabled(actor, "the-key", PatchRequest(false))

        assertNotNull(result)
        assertFalse(result!!.enabled)
        assertEquals(link.id, result.id)
        // setEnabled on an existing link fires the dedicated enabledChanged, not handleUpdated
        verify(exactly = 1) { listener.enabledChanged(actor, result) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
    }

    @Test
    fun setEnabledCreatesWhenMissing() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val result = service.setEnabled(actor, "new-key", PatchRequest(true))

        assertNotNull(result)
        assertEquals("new-key", result!!.key)
        assertTrue(result.enabled)
        // missing link is created -> handleCreated, not enabledChanged
        verify(exactly = 1) { listener.handleCreated(actor, result) }
        verify(exactly = 0) { listener.enabledChanged(any(), any()) }
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val link = service.create(actor, "my-key", LinkTestData.createRequest())
        verify(exactly = 1) { listener.handleCreated(actor, link) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, link.id, LinkTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, link.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}
