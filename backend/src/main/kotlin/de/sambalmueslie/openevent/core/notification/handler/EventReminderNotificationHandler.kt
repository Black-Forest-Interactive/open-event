package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.notification.NotificationEvent
import de.sambalmueslie.openevent.core.notification.NotificationService
import de.sambalmueslie.openevent.core.notification.NotificationSettingCrudService
import de.sambalmueslie.openevent.core.notification.api.NotificationSettingChangeRequest
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantStatus
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.scheduling.annotation.Scheduled
import jakarta.annotation.PostConstruct
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean
import kotlin.system.measureTimeMillis

/**
 * Sends a reminder mail to all accepted participants of events starting on the next day.
 */
@Singleton
class EventReminderNotificationHandler(
    private val eventService: EventCrudService,
    private val accountService: AccountCrudService,
    private val service: NotificationService,
    private val settingService: NotificationSettingCrudService,
    private val timeProvider: TimeProvider
) : NotificationHandler {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventReminderNotificationHandler::class.java)
        const val KEY_EVENT_REMINDER = "event.reminder"
        const val SETTING_NAME = "event.reminder.enabled"
    }

    @PostConstruct
    fun init() {
        val account = accountService.getSystemAccount()
        settingService.findByName(SETTING_NAME) ?: settingService.create(account, NotificationSettingChangeRequest(SETTING_NAME, true))
    }

    override fun getName(): String = EventReminderNotificationHandler::class.java.simpleName

    override fun getTypes(): Set<NotificationTypeChangeRequest> {
        return setOf(
            NotificationTypeChangeRequest(KEY_EVENT_REMINDER, "Event reminder (one day before)", "")
        )
    }

    private val processing = AtomicBoolean(false)

    @Scheduled(cron = "0 0 8 * * ?")
    fun sendDailyReminder() {
        val setting = settingService.findByName(SETTING_NAME)
        if (setting != null && !setting.enabled) return logger.info("Event reminder sending is globally disabled, skipping.")
        if (!processing.compareAndSet(false, true)) return logger.warn("Ignore event reminder, cause previous job hasn't finished yet")
        try {
            logger.info("Starting event reminder notification")
            val duration = measureTimeMillis { sendReminder() }
            logger.info("Finished event reminder notification within $duration ms")
        } catch (e: Throwable) {
            logger.error("Failed to send event reminder notification", e)
        } finally {
            processing.set(false)
        }
    }

    private fun sendReminder() {
        val actor = accountService.getSystemAccount()
        val tomorrow = timeProvider.now().toLocalDate().plusDays(1)
        val events = eventService.getInfosByStart(tomorrow.atStartOfDay(), tomorrow.plusDays(1).atStartOfDay().minusNanos(1))
            .filter { it.event.status == EventStatus.ACTIVE && it.registration != null }
        logger.debug("Found ${events.size} events starting at $tomorrow")
        events.forEach { sendReminder(actor, it) }
    }

    private fun sendReminder(actor: Account, info: EventInfo) {
        val registration = info.registration ?: return
        val participants = registration.participants.filter { isValidParticipant(it) }
        if (participants.isEmpty()) return

        val contact = eventService.getContact(info.event.id)
        participants.forEach { participant ->
            val content = EventReminderContent(info.event, registration.registration, participant, info.location, contact)
            val attachment = EventCalendarAttachment.create(info.event, info.location)
            service.process(NotificationEvent(KEY_EVENT_REMINDER, actor, content, attachments = listOf(attachment)), listOf(participant.author))
        }
        logger.debug("[${info.event.id}] Sent reminder to ${participants.size} participants")
    }

    private fun isValidParticipant(participant: Participant): Boolean {
        return participant.status == ParticipantStatus.ACCEPTED && !participant.waitingList
    }
}
