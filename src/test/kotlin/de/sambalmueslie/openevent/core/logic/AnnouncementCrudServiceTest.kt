package de.sambalmueslie.openevent.core.logic

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.announcement.AnnouncementChangeListener
import de.sambalmueslie.openevent.core.logic.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.core.model.AccountChangeRequest
import de.sambalmueslie.openevent.core.model.Announcement
import de.sambalmueslie.openevent.core.model.AnnouncementChangeRequest
import de.sambalmueslie.openevent.storage.account.AccountStorageService
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

@MicronautTest
class AnnouncementCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorageService

    @Inject
    lateinit var accountService: AccountCrudService

    @Inject
    lateinit var service: AnnouncementCrudService

    @Test
    fun announcementCrud() {
        val actor = accountStorage.create(AccountChangeRequest("user", "first", "last", "email@localhost", "", ""))
        val listener = mockk<AnnouncementChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs

        val author =
            accountService.create(actor, AccountChangeRequest("name", "first-name", "last-name", "email", "", null))

        val request = AnnouncementChangeRequest("subject", "content")
        var announcement = service.create(author, request)
        verify { listener.handleCreated(author, announcement) }

        assertEquals(request.subject, announcement.subject)
        assertEquals(request.content, announcement.content)
        assertEquals(author, announcement.author)
        assertTrue(announcement.id > 0)

        assertEquals(announcement, service.get(announcement.id))
        assertEquals(listOf(announcement), service.getAll(Pageable.from(0)).content)

        val update = AnnouncementChangeRequest("subject-update", "content-update")
        announcement = service.update(actor, announcement.id, update)
        verify { listener.handleUpdated(actor, announcement) }

        assertEquals(update.subject, announcement.subject)
        assertEquals(update.content, announcement.content)
        assertEquals(author, announcement.author)

        service.delete(actor, announcement.id)
        verify { listener.handleDeleted(actor, announcement) }
        assertEquals(emptyList<Announcement>(), service.getAll(Pageable.from(0)).content)

    }
}
