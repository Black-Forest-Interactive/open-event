package de.sambalmueslie.openevent.core.logic.feedback

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.feedback.FeedbackChangeListener
import de.sambalmueslie.openevent.core.feedback.FeedbackCrudService
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.FeedbackTestData
import de.sambalmueslie.openevent.testdata.TestHttpRequest
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class FeedbackCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: FeedbackCrudService

    private fun registerListener(): FeedbackChangeListener {
        val listener = mockk<FeedbackChangeListener>()
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

        val request = FeedbackTestData.createRequest()
        val feedback = service.create(actor, actor, request, TestHttpRequest.mock())

        assertTrue(feedback.id > 0)
        assertEquals(request.subject, feedback.subject)
        assertEquals(request.description, feedback.description)
        assertEquals(request.topic, feedback.topic)
        assertEquals(request.tags, feedback.tags)
        assertEquals(request.rating, feedback.rating)
        assertEquals(actor, feedback.account)
        assertEquals("127.0.0.1", feedback.clientIp)
        assertEquals("test-agent", feedback.userAgent)
        verify { listener.handleCreated(actor, feedback) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val feedback = FeedbackTestData.create(service, actor, actor)

        assertEquals(feedback, service.get(feedback.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = FeedbackTestData.create(service, actor, actor, FeedbackTestData.createRequest(subject = "first"))
        val second = FeedbackTestData.create(service, actor, actor, FeedbackTestData.createRequest(subject = "second"))

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val feedback = FeedbackTestData.create(service, actor, actor)
        val listener = registerListener()

        val request = FeedbackTestData.updateRequest()
        val updated = service.update(actor, feedback.id, request)

        assertEquals(feedback.id, updated.id)
        assertEquals(request.subject, updated.subject)
        assertEquals(request.topic, updated.topic)
        assertEquals(request.rating, updated.rating)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val feedback = FeedbackTestData.create(service, actor, actor)
        val listener = registerListener()

        val deleted = service.delete(actor, feedback.id)

        assertEquals(feedback, deleted)
        assertNull(service.get(feedback.id))
        verify(exactly = 1) { listener.handleDeleted(actor, feedback) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun getByAccount() {
        val actor = AccountTestData.createActor(accountStorage)
        val other = accountStorage.create(AccountTestData.accountRequest(externalId = "other"))
        val feedback = FeedbackTestData.create(service, actor, actor)

        assertEquals(listOf(feedback), service.getByAccount(actor, Pageable.from(0)).content)
        assertTrue(service.getByAccount(other, Pageable.from(0)).content.isEmpty())
    }

    @Test
    fun getByTopic() {
        val actor = AccountTestData.createActor(accountStorage)
        val feedback = FeedbackTestData.create(service, actor, actor, FeedbackTestData.createRequest(topic = "billing"))

        assertEquals(listOf(feedback), service.getByTopic("billing", Pageable.from(0)).content)
        assertTrue(service.getByTopic("unknown", Pageable.from(0)).content.isEmpty())
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val feedback = service.create(actor, actor, FeedbackTestData.createRequest(), TestHttpRequest.mock())
        verify(exactly = 1) { listener.handleCreated(actor, feedback) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, feedback.id, FeedbackTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, feedback.id)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}
