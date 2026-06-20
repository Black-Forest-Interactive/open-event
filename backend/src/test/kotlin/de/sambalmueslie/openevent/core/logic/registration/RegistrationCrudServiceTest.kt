package de.sambalmueslie.openevent.core.logic.registration

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus
import de.sambalmueslie.openevent.core.registration.RegistrationChangeListener
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.EventTestData
import de.sambalmueslie.openevent.testdata.ParticipantTestData
import de.sambalmueslie.openevent.testdata.RegistrationTestData
import io.micronaut.data.model.Pageable
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class RegistrationCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var eventService: EventCrudService

    @Inject
    lateinit var service: RegistrationCrudService

    private fun createEvent(actor: Account): Event =
        EventTestData.create(eventService, actor, EventTestData.createRequest(location = null))

    private fun guest(externalId: String = "guest"): Account =
        accountStorage.create(AccountTestData.accountRequest(externalId = externalId))

    private fun registerListener(): RegistrationChangeListener {
        val listener = mockk<RegistrationChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        every { listener.participantCreated(any(), any(), any(), any()) } just Runs
        every { listener.participantChanged(any(), any(), any(), any()) } just Runs
        every { listener.participantRemoved(any(), any(), any()) } just Runs
        return listener
    }

    @Test
    fun create() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        // the registration is auto-created as part of event creation
        val event = createEvent(actor)
        val registration = service.findByEvent(event)!!

        assertTrue(registration.id > 0)
        assertEquals(event.id, registration.eventId)
        assertEquals(10, registration.maxGuestAmount)
        assertTrue(registration.interestedAllowed)
        assertFalse(registration.ticketsEnabled)
        verify { listener.handleCreated(actor, registration) }
    }

    @Test
    fun get() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = service.findByEvent(createEvent(actor))!!

        assertEquals(registration, service.get(registration.id))
        assertNull(service.get(-1))
    }

    @Test
    fun getAll() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = service.findByEvent(createEvent(actor))!!
        val second = service.findByEvent(createEvent(actor))!!

        assertEquals(setOf(first, second), service.getAll(Pageable.from(0)).content.toSet())
    }

    @Test
    fun updateByEventUpdatesWhenExisting() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        val listener = registerListener()

        val result = service.updateByEvent(actor, event, RegistrationTestData.updateRequest())

        assertEquals(20, result.maxGuestAmount)
        assertTrue(result.ticketsEnabled)
        verify { listener.handleUpdated(actor, result) }
    }

    @Test
    fun updateByEventCreatesWhenMissing() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        service.deleteByEvent(actor, event)
        val listener = registerListener()

        val result = service.updateByEvent(actor, event, RegistrationTestData.createRequest())

        assertEquals(event.id, result.eventId)
        verify { listener.handleCreated(actor, result) }
    }

    @Test
    fun deleteByEvent() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)
        val registration = service.findByEvent(event)!!
        val listener = registerListener()

        val deleted = service.deleteByEvent(actor, event)

        assertEquals(registration, deleted)
        assertNull(service.findByEvent(event))
        verify(exactly = 1) { listener.handleDeleted(actor, registration) }
    }

    @Test
    fun isValidRejectsInvalidMaxGuestOnCreate() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)

        assertThrows(InvalidRequestException::class.java) {
            service.create(actor, event, RegistrationTestData.createRequest(maxGuestAmount = 0))
        }
    }

    @Test
    fun isValidRejectsInvalidMaxGuestOnUpdate() {
        val actor = AccountTestData.createActor(accountStorage)
        val event = createEvent(actor)

        assertThrows(InvalidRequestException::class.java) {
            service.updateByEvent(actor, event, RegistrationTestData.updateRequest(maxGuestAmount = 0))
        }
    }

    @Test
    fun findByEventIds() {
        val actor = AccountTestData.createActor(accountStorage)
        val first = service.findByEvent(createEvent(actor))!!
        val second = service.findByEvent(createEvent(actor))!!

        assertEquals(setOf(first, second), service.findByEventIds(setOf(first.eventId, second.eventId)).toSet())
    }

    @Test
    fun addAndRemoveParticipant() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = service.findByEvent(createEvent(actor))!!
        val guest = guest()
        val listener = registerListener()

        val response = service.addParticipant(actor, registration.id, guest, ParticipantTestData.participateRequest(2))
        assertNotNull(response)
        assertNotNull(response!!.participant)
        assertEquals(ParticipateStatus.ACCEPTED, response.status)
        verify { listener.participantCreated(actor, registration, any(), ParticipateStatus.ACCEPTED) }
        assertEquals(1, service.getParticipants(registration.id).size)

        val removed = service.removeParticipant(actor, registration.id, guest)
        assertNotNull(removed)
        verify { listener.participantRemoved(actor, registration, any()) }
        assertTrue(service.getParticipants(registration.id).isEmpty())
    }

    @Test
    fun getInfoAndDetails() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = service.findByEvent(createEvent(actor))!!
        service.addParticipant(actor, registration.id, guest(), ParticipantTestData.participateRequest(2))

        val info = service.getInfo(registration.id)
        assertNotNull(info)
        assertEquals(registration, info!!.registration)
        assertEquals(1, info.participants.size)

        val details = service.getDetails(registration.id)
        assertNotNull(details)
        assertEquals(1, details!!.participants.size)
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val listener = registerListener()

        val event = createEvent(actor)
        val registration = service.findByEvent(event)!!
        verify(exactly = 1) { listener.handleCreated(actor, registration) }
        verify(exactly = 0) { listener.handleUpdated(any(), any()) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        val updated = service.updateByEvent(actor, event, RegistrationTestData.updateRequest())
        verify(exactly = 1) { listener.handleUpdated(actor, updated) }

        service.deleteByEvent(actor, event)
        verify(exactly = 1) { listener.handleDeleted(actor, updated) }
    }

}
