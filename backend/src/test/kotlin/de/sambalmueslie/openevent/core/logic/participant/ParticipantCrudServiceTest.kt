package de.sambalmueslie.openevent.core.logic.participant

import de.sambalmueslie.openevent.TimeBasedTest
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.participant.ParticipantChangeListener
import de.sambalmueslie.openevent.core.participant.ParticipantCrudService
import de.sambalmueslie.openevent.core.participant.api.ParticipantStatus
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest
import de.sambalmueslie.openevent.testdata.AccountTestData
import de.sambalmueslie.openevent.testdata.EventTestData
import de.sambalmueslie.openevent.testdata.ParticipantTestData
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import io.mockk.*
import jakarta.inject.Inject
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@MicronautTest
class ParticipantCrudServiceTest : TimeBasedTest() {

    @Inject
    lateinit var accountStorage: AccountStorage

    @Inject
    lateinit var eventService: EventCrudService

    @Inject
    lateinit var registrationService: RegistrationCrudService

    @Inject
    lateinit var service: ParticipantCrudService

    private fun registration(actor: Account, maxGuestAmount: Int = 10): Registration {
        val event = EventTestData.create(
            eventService, actor,
            EventTestData.createRequest(location = null, registration = RegistrationChangeRequest(maxGuestAmount, true, false)),
        )
        return registrationService.findByEvent(event)!!
    }

    private fun guest(externalId: String = "guest"): Account =
        accountStorage.create(AccountTestData.accountRequest(externalId = externalId))

    private fun registerListener(): ParticipantChangeListener {
        val listener = mockk<ParticipantChangeListener>()
        service.register(listener)
        every { listener.handleCreated(any(), any()) } just Runs
        every { listener.handleUpdated(any(), any()) } just Runs
        every { listener.handleDeleted(any(), any()) } just Runs
        return listener
    }

    @Test
    fun changeCreatesParticipant() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = registration(actor)
        val guest = guest()
        val listener = registerListener()

        val response = service.change(actor, registration, guest, ParticipantTestData.participateRequest(2))

        assertTrue(response.created)
        val participant = response.participant!!
        assertEquals(2L, participant.size)
        assertEquals("note", participant.note)
        assertEquals(ParticipantStatus.ACCEPTED, participant.status)
        assertFalse(participant.waitingList)
        assertEquals(guest.id, participant.author.id)
        assertEquals(ParticipateStatus.ACCEPTED, response.status)
        verify { listener.handleCreated(actor, participant) }
    }

    @Test
    fun getReturnsParticipants() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = registration(actor)
        service.change(actor, registration, guest(), ParticipantTestData.participateRequest(2))

        assertEquals(1, service.get(registration).size)
    }

    @Test
    fun getDetails() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = registration(actor)
        service.change(actor, registration, guest(), ParticipantTestData.participateRequest(2))

        assertEquals(1, service.getDetails(registration).size)
    }

    @Test
    fun changeUpdatesExistingSize() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = registration(actor)
        val guest = guest()
        service.change(actor, registration, guest, ParticipantTestData.participateRequest(2))

        service.change(actor, registration, guest, ParticipantTestData.participateRequest(4))

        val participants = service.get(registration)
        assertEquals(1, participants.size)
        assertEquals(4L, participants.first().size)
    }

    @Test
    fun remove() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = registration(actor)
        val guest = guest()
        service.change(actor, registration, guest, ParticipantTestData.participateRequest(2))
        val listener = registerListener()

        service.remove(actor, registration, guest)

        assertTrue(service.get(registration).isEmpty())
        verify(exactly = 1) { listener.handleDeleted(actor, any()) }
    }

    @Test
    fun waitingListWhenFull() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = registration(actor, maxGuestAmount = 1)
        val guest = guest()

        val response = service.change(actor, registration, guest, ParticipantTestData.participateRequest(2))

        assertEquals(ParticipateStatus.WAITING_LIST, response.status)
        assertTrue(response.participant!!.waitingList)
    }

    @Test
    fun deleteByRegistration() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = registration(actor)
        service.change(actor, registration, guest(), ParticipantTestData.participateRequest(2))
        val listener = registerListener()

        service.deleteByRegistration(actor, registration)

        assertTrue(service.get(registration).isEmpty())
        verify(exactly = 1) { listener.handleDeleted(actor, any()) }
    }

    @Test
    fun notifications() {
        val actor = AccountTestData.createActor(accountStorage)
        val registration = registration(actor)
        val guest = guest()
        val listener = registerListener()

        val response = service.change(actor, registration, guest, ParticipantTestData.participateRequest(2))
        verify(exactly = 1) { listener.handleCreated(actor, response.participant!!) }
        verify(exactly = 0) { listener.handleDeleted(any(), any()) }

        service.remove(actor, registration, guest)
        verify(exactly = 1) { listener.handleDeleted(actor, any()) }
    }

}
