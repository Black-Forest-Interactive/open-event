package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.notification.NotificationEvent
import de.sambalmueslie.openevent.core.notification.NotificationService
import de.sambalmueslie.openevent.core.notification.NotificationSettingCrudService
import de.sambalmueslie.openevent.core.notification.api.NotificationSetting
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantStatus
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class EventReminderNotificationHandlerTest {

    private val eventService = mockk<EventCrudService>(relaxed = true)
    private val accountService = mockk<AccountCrudService>(relaxed = true)
    private val notificationService = mockk<NotificationService>(relaxed = true)
    private val settingService = mockk<NotificationSettingCrudService>(relaxed = true)
    private val now = LocalDateTime.of(2026, 3, 10, 8, 0)

    private val actor = mockk<Account>(relaxed = true)
    private val owner = AccountInfo(1, "owner", "", "owner@test.de", "First", "Last", "de")

    private fun handler() = EventReminderNotificationHandler(
        eventService, accountService, notificationService, settingService
    ) { now }

    private fun event(id: Long, status: EventStatus = EventStatus.ACTIVE) = Event(
        id = id,
        owner = owner,
        start = now.plusDays(1).withHour(18),
        finish = now.plusDays(1).withHour(20),
        title = "Event $id",
        shortText = "",
        longText = "",
        imageUrl = "",
        iconUrl = "",
        featured = false,
        hasLocation = false,
        hasRegistration = true,
        status = status,
        published = true,
        tags = emptySet(),
        created = now,
        changed = null,
    )

    private fun participant(id: Long, status: ParticipantStatus = ParticipantStatus.ACCEPTED, waitingList: Boolean = false) = Participant(
        id = id,
        size = 1,
        note = "",
        status = status,
        rank = 0,
        waitingList = waitingList,
        author = AccountInfo(100 + id, "p$id", "", "p$id@test.de", "P", "$id", "de"),
        timestamp = now,
    )

    private fun info(event: Event, participants: List<Participant>?) = EventInfo(
        event = event,
        location = null,
        registration = participants?.let { RegistrationInfo(Registration(event.id, event.id, 10, false, false), it) },
        audiences = emptyList(),
        categories = emptyList(),
        share = null,
        bookmarked = false,
        canEdit = false,
    )

    @Test
    fun sendsReminderOnlyToAcceptedParticipantsOfActiveEventsTomorrow() {
        every { accountService.getSystemAccount() } returns actor
        every { settingService.findByName(any()) } returns NotificationSetting(1, EventReminderNotificationHandler.SETTING_NAME, true)
        val accepted = participant(1)
        val waiting = participant(2, waitingList = true)
        val canceled = participant(3, ParticipantStatus.CANCELED)
        every { eventService.getInfosByStart(any(), any()) } returns listOf(
            info(event(1), listOf(accepted, waiting, canceled)),
            info(event(2, EventStatus.CANCELED), listOf(participant(4))),
            info(event(3), null),
        )

        handler().sendDailyReminder()

        verify { eventService.getInfosByStart(now.toLocalDate().plusDays(1).atStartOfDay(), match { it.toLocalDate() == now.toLocalDate().plusDays(1) }) }
        val events = mutableListOf<NotificationEvent<EventReminderContent>>()
        val recipients = mutableListOf<Collection<AccountInfo>>()
        verify(exactly = 1) { notificationService.process(capture(events), capture(recipients)) }
        assertEquals(EventReminderNotificationHandler.KEY_EVENT_REMINDER, events.single().key)
        assertEquals(accepted, events.single().content.participant)
        assertEquals(listOf(accepted.author), recipients.single())
    }

    @Test
    fun doesNothingWhenGloballyDisabled() {
        every { settingService.findByName(any()) } returns NotificationSetting(1, EventReminderNotificationHandler.SETTING_NAME, false)

        handler().sendDailyReminder()

        verify(exactly = 0) { eventService.getInfosByStart(any(), any()) }
        verify(exactly = 0) { notificationService.process(any<NotificationEvent<Any>>(), any()) }
    }
}
