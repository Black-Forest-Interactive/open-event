package de.sambalmueslie.openevent.core.participant

import de.sambalmueslie.openevent.config.AppConfig
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.notification.handler.ExternalParticipantNotificationHandler
import de.sambalmueslie.openevent.core.participant.api.ExternalParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus
import de.sambalmueslie.openevent.core.participant.db.ExternalParticipantData
import de.sambalmueslie.openevent.core.participant.db.ExternalParticipantRepository
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class ExternalParticipantServiceTest {

    private val accountService = mockk<AccountCrudService>(relaxed = true)
    private val settingsService = mockk<SettingsService>(relaxed = true)
    private val registrationService = mockk<RegistrationCrudService>(relaxed = true)
    private val notificationHandler = mockk<ExternalParticipantNotificationHandler>(relaxed = true)
    private val repository = mockk<ExternalParticipantRepository>(relaxed = true)
    private var now = LocalDateTime.of(2026, 3, 10, 18, 0)

    private val service = ExternalParticipantService(accountService, settingsService, registrationService, notificationHandler, repository, { now }, AppConfig())

    private val share = mockk<Share>(relaxed = true)
    private val event = mockk<EventInfo>(relaxed = true)
    private fun request(firstName: String = "Paula", phone: String = "", size: Long = 2) = ExternalParticipantAddRequest(firstName, "Participant", "paula@test.de", phone, "", size)
    private fun existing(created: LocalDateTime) = ExternalParticipantData("id", 1, "Paula", "Participant", "paula@test.de", "", "", "de", 2, created.plusHours(24), "12345", 0, created, null)

    @Test
    fun knownAccountIsNotRegisteredWithoutConfirmation() {
        every { repository.findByEventIdAndEmail(any(), any()) } returns null
        every { repository.save(any<ExternalParticipantData>()) } answers { firstArg() }

        val response = service.requestParticipation(share, event, request(), "de")

        assertEquals(ParticipateStatus.UNCONFIRMED, response.status)
        assertNull(response.participant)
        verify(exactly = 0) { accountService.findByEmail(any()) }
        verify(exactly = 0) { registrationService.addParticipant(any(), any(), any(), any<de.sambalmueslie.openevent.core.participant.api.ParticipateRequest>()) }
        verify(exactly = 1) { notificationHandler.handleCreated(any(), any(), any(), any(), any()) }
    }

    @Test
    fun repeatedRequestDoesNotSendAnotherMailWithinDelay() {
        every { repository.findByEventIdAndEmail(any(), any()) } returns existing(now.minusMinutes(2))

        val response = service.requestParticipation(share, event, request(), "de")

        assertEquals(ParticipateStatus.UNCONFIRMED, response.status)
        verify(exactly = 0) { notificationHandler.handleCreated(any(), any(), any(), any(), any()) }
    }

    @Test
    fun repeatedRequestSendsMailAgainAfterDelay() {
        val data = existing(now.minusMinutes(30))
        every { repository.findByEventIdAndEmail(any(), any()) } returns data
        every { repository.update(any<ExternalParticipantData>()) } answers { firstArg() }

        service.requestParticipation(share, event, request(), "de")

        verify(exactly = 1) { notificationHandler.handleCreated(any(), any(), any(), any(), any()) }
    }

    @Test
    fun rejectsInvalidInput() {
        assertThrows(InvalidRequestException::class.java) { service.requestParticipation(share, event, request(firstName = "<a href=x>"), "de") }
        assertThrows(InvalidRequestException::class.java) { service.requestParticipation(share, event, request(firstName = "x".repeat(101)), "de") }
        assertThrows(InvalidRequestException::class.java) { service.requestParticipation(share, event, request(phone = "call me"), "de") }
        assertThrows(InvalidRequestException::class.java) { service.requestParticipation(share, event, request(size = 11), "de") }
        verify(exactly = 0) { notificationHandler.handleCreated(any(), any(), any(), any(), any()) }
    }
}
