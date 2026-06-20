package de.sambalmueslie.openevent.core.logic.announcement

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.announcement.AnnouncementChangeListener
import de.sambalmueslie.openevent.core.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.AnnouncementTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class AnnouncementCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var service: AnnouncementCrudService

    private fun registerListener(): AnnouncementChangeListener {
        val listener = mockk<AnnouncementChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val author = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val request = AnnouncementTestData.createRequest()
        val announcement = service.create(author, request)

        assertTrue(announcement.id > 0)
        assertEquals(request.subject, announcement.subject)
        assertEquals(request.content, announcement.content)
        assertEquals(author, announcement.author)
        verify { listener.handleCreated(author, announcement) }
    }

    @Test
    fun get() {
        val author = AccountTestData.createActor(accountStorage)
        val announcement = AnnouncementTestData.create(service, author)

        assertEquals(announcement, service.get(announcement.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val author = AccountTestData.createActor(accountStorage)
        val first = AnnouncementTestData.create(service, author, AnnouncementTestData.createRequest("first"))
        val second = AnnouncementTestData.create(service, author, AnnouncementTestData.createRequest("second"))

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun update() {
        val author = AccountTestData.createActor(accountStorage)
        val announcement = AnnouncementTestData.create(service, author)
        val listener = registerListener()

        val request = AnnouncementTestData.updateRequest()
        val updated = service.update(author, announcement.id, request)

        assertEquals(announcement.id, updated.id)
        assertEquals(request.subject, updated.subject)
        assertEquals(request.content, updated.content)
        verify { listener.handleUpdated(author, updated) }
    }

    @Test
    fun delete() {
        val author = AccountTestData.createActor(accountStorage)
        val announcement = AnnouncementTestData.create(service, author)
        val listener = registerListener()

        val deleted = service.delete(author, announcement.id)

        assertEquals(announcement, deleted)
        assertNull(service.get(announcement.id))
        verify(exactly = 1) { listener.handleDeleted(author, announcement) }
        verify(exactly = 0) { listener.handleCreated(any(), any()) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
    }

    @Test
    fun isValidRejectsBlankSubjectOnCreate() {
        val author = AccountTestData.createActor(accountStorage)

        assertThrows(InvalidRequestException::class.java) {
            service.create(author, AnnouncementTestData.createRequest(subject = " "))
        }
    }

    @Test
    fun isValidRejectsBlankSubjectOnUpdate() {
        val author = AccountTestData.createActor(accountStorage)
        val announcement = AnnouncementTestData.create(service, author)

        assertThrows(InvalidRequestException::class.java) {
            service.update(author, announcement.id, AnnouncementTestData.updateRequest(subject = " "))
        }
    }

    @Test
    fun notifications() {
        val author = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val announcement = service.create(author, AnnouncementTestData.createRequest())
        verify(exactly = 1) { listener.handleCreated(author, announcement) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.update(author, announcement.id, AnnouncementTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(author, updated) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.delete(author, announcement.id)
        verify(exactly = 1) { listener.handleDeleted(author, updated) }
    }

}
