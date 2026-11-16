package de.sambalmueslie.openevent.core.logic.finder

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.finder.FinderCrudService
import de.sambalmueslie.openevent.core.finder.api.Finder
import de.sambalmueslie.openevent.core.finder.api.FinderAnswer
import de.sambalmueslie.openevent.core.finder.api.FinderChangeRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.event.EventSearchOperator
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.EventTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime

@MicronautTest
class FinderCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var eventService: EventCrudService

    @Inject
    lateinit var searchOperator: EventSearchOperator

    @Inject
    lateinit var service: FinderCrudService

    @BeforeEach
    fun setup() {
        searchOperator.setup()
    }

    private fun createEvent(actor: Account, day: LocalDate, status: EventStatus = EventStatus.ACTIVE): Event {
        val start = LocalDateTime.of(day.year, day.month, day.dayOfMonth, 20, 0)
        return EventTestData.create(eventService, actor, EventTestData.createRequest(start = start, finish = start.plusHours(2), status = status))
    }

    private fun createFinder(actor: Account, from: LocalDate, to: LocalDate): Finder {
        val request = EventSearchRequest("", from, to, false, false, false, false, false)
        return service.create(actor, FinderChangeRequest("finder", request))
    }

    @Test
    fun createRejectsMissingRange() {
        val actor = AccountTestData.createActor(accountStorage)
        val request = EventSearchRequest("", null, null, false, false, false, false, false)
        assertThrows(InvalidRequestException::class.java) {
            service.create(actor, FinderChangeRequest("finder", request))
        }
    }

    @Test
    fun closedAndDeletedEventsDropOut() {
        val actor = AccountTestData.createActor(accountStorage)
        val from = LocalDate.of(2032, 5, 1)
        val canceled = createEvent(actor, from.plusDays(1))
        val deleted = createEvent(actor, from.plusDays(2))
        val finder = createFinder(actor, from, from.plusDays(10))

        service.setAnswer(actor, finder, canceled.id, FinderAnswer.YES)
        service.setAnswer(actor, finder, deleted.id, FinderAnswer.YES)
        assertEquals(2, service.getEntries(actor, finder, FinderAnswer.YES, Pageable.from(0)).totalSize)

        eventService.cancel(actor, canceled.id, "reason")
        val entries = service.getEntries(actor, finder, FinderAnswer.YES, Pageable.from(0))
        assertEquals(listOf(deleted.id.toString()), entries.content.map { it.entry.id })

        eventService.delete(actor, deleted.id)
        assertEquals(0, service.getEntries(actor, finder, FinderAnswer.YES, Pageable.from(0)).totalSize)
        assertEquals(0, service.getInfo(actor, finder).countYes)
    }

    @Test
    fun resetAndDelete() {
        val actor = AccountTestData.createActor(accountStorage)
        val from = LocalDate.of(2033, 7, 1)
        val event = createEvent(actor, from.plusDays(1))
        val finder = createFinder(actor, from, from.plusDays(10))

        service.setAnswer(actor, finder, event.id, FinderAnswer.NO)
        assertEquals(0, service.getOpen(actor, finder, Pageable.from(0, 10)).totalSize)

        service.resetAnswers(actor, finder)
        assertEquals(listOf(event.id.toString()), service.getOpen(actor, finder, Pageable.from(0, 10)).content.map { it.id })

        service.setAnswer(actor, finder, event.id, FinderAnswer.YES)
        service.delete(actor, finder.id)
        assertNull(service.get(finder.id))
    }

    @Test
    fun openCandidatesAndPreview() {
        val actor = AccountTestData.createActor(accountStorage)
        val from = LocalDate.of(2034, 9, 1)
        val first = createEvent(actor, from.plusDays(1))
        val second = createEvent(actor, from.plusDays(2))
        createEvent(actor, from.plusDays(3), EventStatus.CANCELED)
        createEvent(actor, from.plusDays(40))
        val finder = createFinder(actor, from, from.plusDays(10))

        // canceled events and events outside the range are no candidates
        assertEquals(listOf(first.id.toString(), second.id.toString()), service.getOpen(actor, finder, Pageable.from(0, 10)).content.map { it.id })

        val preview = service.preview(actor, FinderChangeRequest("preview", finder.request))
        assertEquals(2, preview.total)

        service.setAnswer(actor, finder, first.id, FinderAnswer.YES)
        val open = service.getOpen(actor, finder, Pageable.from(0, 10))
        assertEquals(listOf(second.id.toString()), open.content.map { it.id })
    }
}
