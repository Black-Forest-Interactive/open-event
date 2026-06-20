package de.sambalmueslie.openevent.core.logic.issue

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.issue.IssueChangeListener
import de.sambalmueslie.openevent.core.issue.IssueCrudService
import de.sambalmueslie.openevent.core.issue.api.IssueStatus
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.IssueTestData
import de.sambalmueslie.openevent.testdata.TestHttpRequest
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class IssueCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: IssueCrudService

    private fun registerListener(): IssueChangeListener {
        val listener = mockk<IssueChangeListener>()
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

        val request = IssueTestData.createRequest()
        val issue = service.create(actor, actor, request, TestHttpRequest.mock())

        assertTrue(issue.id > 0)
        assertEquals(request.subject, issue.subject)
        assertEquals(request.description, issue.description)
        assertEquals(request.error, issue.error)
        assertEquals(request.url, issue.url)
        assertEquals(actor, issue.account)
        assertEquals(IssueStatus.CREATED, issue.status)
        assertEquals("127.0.0.1", issue.clientIp)
        assertEquals("test-agent", issue.userAgent)
        verify { listener.handleCreated(actor, issue) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val issue = IssueTestData.create(service, actor, actor)

        assertEquals(issue, service.get(issue.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = IssueTestData.create(service, actor, actor, IssueTestData.createRequest(subject = "first"))
        val second = IssueTestData.create(service, actor, actor, IssueTestData.createRequest(subject = "second"))

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val actor = AccountTestData.createActor(accountStorage)
        val issue = IssueTestData.create(service, actor, actor)
        val listener = registerListener()

        val request = IssueTestData.updateRequest()
        val updated = service.update(actor, issue.id, request)

        assertEquals(issue.id, updated.id)
        assertEquals(request.subject, updated.subject)
        assertEquals(request.error, updated.error)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun delete() {
        val actor = AccountTestData.createActor(accountStorage)
        val issue = IssueTestData.create(service, actor, actor)
        val listener = registerListener()

        val deleted = service.delete(actor, issue.id)

        assertEquals(issue, deleted)
        assertNull(service.get(issue.id))
        verify(exactly = 1) { listener.handleDeleted(actor, issue) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun getByAccount() {
        val actor = AccountTestData.createActor(accountStorage)
        val other = accountStorage.create(AccountTestData.accountRequest(externalId = "other"))
        val issue = IssueTestData.create(service, actor, actor)

        assertEquals(listOf(issue), service.getByAccount(actor, Pageable.from(0)).content)
        assertTrue(service.getByAccount(other, Pageable.from(0)).content.isEmpty())
    }

    @Test
    fun getUnresolved() {
        val actor = AccountTestData.createActor(accountStorage)
        val issue = IssueTestData.create(service, actor, actor)

        assertEquals(listOf(issue), service.getUnresolved(Pageable.from(0)).content)
        assertEquals(listOf(issue), service.getUnresolvedByAccount(actor, Pageable.from(0)).content)

        service.changeStatus(actor, issue.id, PatchRequest(IssueStatus.RESOLVED))
        assertTrue(service.getUnresolved(Pageable.from(0)).content.isEmpty())
        assertTrue(service.getUnresolvedByAccount(actor, Pageable.from(0)).content.isEmpty())
    }

    @Test
    fun changeStatus() {
        val actor = AccountTestData.createActor(accountStorage)
        val issue = IssueTestData.create(service, actor, actor)
        val listener = registerListener()

        val updated = service.changeStatus(actor, issue.id, PatchRequest(IssueStatus.RESOLVED))

        assertNotNull(updated)
        assertEquals(IssueStatus.RESOLVED, updated!!.status)
        verify { listener.handleUpdated(actor, updated) }
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val issue = service.create(actor, actor, IssueTestData.createRequest(), TestHttpRequest.mock())
        verify(exactly = 1) { listener.handleCreated(actor, issue) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(actor, issue.id, IssueTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }

        // changeStatus also notifies via handleUpdated
        service.changeStatus(actor, issue.id, PatchRequest(IssueStatus.IN_PROGRESS))
        verify(exactly = 2) { listener.handleUpdated(actor, any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(actor, issue.id)
        verify(exactly = 1) { listener.handleDeleted(actor, any()) }
    }

}
