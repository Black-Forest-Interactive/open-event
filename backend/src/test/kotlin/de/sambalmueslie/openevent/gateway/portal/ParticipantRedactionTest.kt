package de.sambalmueslie.openevent.gateway.portal

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantStatus
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.gateway.portal.event.redactOwnerFor
import de.sambalmueslie.openevent.gateway.portal.registration.redactFor
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ParticipantRedactionTest {

    private val now = LocalDateTime.of(2026, 3, 10, 18, 0)
    private fun info(id: Long) = AccountInfo(id, "user$id", "icon", "user$id@test.de", "First$id", "Last$id", "de")
    private fun account(id: Long) = mockk<Account> { every { this@mockk.id } returns id }
    private fun participant(id: Long, accountId: Long, size: Long) = Participant(id, size, "note$id", ParticipantStatus.ACCEPTED, id.toInt(), false, info(accountId), now)

    private val event = Event(1, info(1), now, now.plusHours(2), "Event", "", "", "", "", false, false, true, EventStatus.ACTIVE, true, emptySet(), now, null)
    private val participants = listOf(participant(1, 2, 2), participant(2, 3, 4))
    private val eventInfo = EventInfo(event, null, RegistrationInfo(Registration(1, 1, 10, false, false), participants), emptyList(), emptyList(), null, false, false)

    @Test
    fun organizerSeesEverything() {
        assertEquals(eventInfo, eventInfo.redactFor(account(1)))
    }

    @Test
    fun participantSeesOnlyOwnEntry() {
        val result = eventInfo.redactFor(account(2)).registration!!.participants
        assertEquals(participants[0], result[0])
        assertRedacted(result[1], participants[1])
    }

    @Test
    fun strangerSeesOnlyAnonymousEntries() {
        val result = eventInfo.redactFor(account(99)).registration!!.participants
        result.forEachIndexed { index, p -> assertRedacted(p, participants[index]) }
        assertEquals(6, result.sumOf { it.size })
    }

    @Test
    fun organizerEmailIsOnlyVisibleToOrganizer() {
        assertEquals("user1@test.de", eventInfo.redactFor(account(1)).event.owner.email)
        assertEquals("user1@test.de", event.redactOwnerFor(account(1)).owner.email)

        val redacted = eventInfo.redactFor(account(2)).event.owner
        assertEquals("", redacted.email)
        assertEquals("First1", redacted.firstName)
        assertEquals("", event.redactOwnerFor(account(99)).owner.email)
    }

    private fun assertRedacted(actual: Participant, original: Participant) {
        assertEquals(original.id, actual.id)
        assertEquals(original.size, actual.size)
        assertEquals(original.waitingList, actual.waitingList)
        assertEquals(original.rank, actual.rank)
        assertEquals("", actual.note)
        assertEquals(0, actual.author.id)
        assertTrue(listOf(actual.author.name, actual.author.email, actual.author.firstName, actual.author.lastName, actual.author.iconUrl).all { it.isEmpty() })
    }
}
