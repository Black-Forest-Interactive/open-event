package de.sambalmueslie.openevent.core.logic.notification.handler


import biweekly.Biweekly
import biweekly.ICalendar
import biweekly.component.VEvent
import biweekly.property.Summary
import de.sambalmueslie.openevent.core.logic.event.EventCrudService
import de.sambalmueslie.openevent.core.logic.notification.NotificationEvent
import de.sambalmueslie.openevent.core.logic.notification.NotificationService
import de.sambalmueslie.openevent.core.logic.registration.RegistrationChangeListener
import de.sambalmueslie.openevent.core.logic.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.infrastructure.mail.api.Attachment
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.nio.charset.Charset
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Singleton
class RegistrationNotificationHandler(
    private val registrationService: RegistrationCrudService,
    private val eventService: EventCrudService,
    private val service: NotificationService,
) : NotificationHandler, RegistrationChangeListener {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RegistrationNotificationHandler::class.java)
        const val KEY_REGISTRATION_CREATED = "registration.create"
        const val KEY_REGISTRATION_UPDATED = "registration.update"
        const val KEY_REGISTRATION_DELETED = "registration.delete"
        const val KEY_PARTICIPANT_CHANGED = "registration.participant.changed"
        const val KEY_PARTICIPANT_REMOVED = "registration.participant.removed"
        const val KEY_PARTICIPANT_ACCEPTED = "registration.participant.accepted"
        const val KEY_PARTICIPANT_DECLINED = "registration.participant.declined"
        const val KEY_PARTICIPANT_WAITLIST = "registration.participant.waitlist"
        private val ZONE_OFFSET = ZoneId.of("Europe/Berlin")
    }

    override fun getName(): String = RegistrationNotificationHandler::class.java.simpleName

    override fun getTypes(): Set<NotificationTypeChangeRequest> {
        return setOf(
            NotificationTypeChangeRequest(KEY_REGISTRATION_CREATED, "Registration created", ""),
            NotificationTypeChangeRequest(KEY_REGISTRATION_UPDATED, "Registration changed", ""),
            NotificationTypeChangeRequest(KEY_REGISTRATION_DELETED, "Registration deleted", ""),
            NotificationTypeChangeRequest(KEY_PARTICIPANT_CHANGED, "Registration participant changed", ""),
            NotificationTypeChangeRequest(KEY_PARTICIPANT_REMOVED, "Registration participant removed", ""),
            NotificationTypeChangeRequest(KEY_PARTICIPANT_ACCEPTED, "Registration participant accepted", ""),
            NotificationTypeChangeRequest(KEY_PARTICIPANT_DECLINED, "Registration participant declined", ""),
            NotificationTypeChangeRequest(KEY_PARTICIPANT_WAITLIST, "Registration participant waitlist", ""),
        )
    }

    init {
        registrationService.register(this)
    }

    override fun handleCreated(actor: Account, obj: Registration) {
        service.process(NotificationEvent(KEY_REGISTRATION_CREATED, actor, obj), getRecipients(actor, obj))
    }


    override fun handleUpdated(actor: Account, obj: Registration) {
        service.process(NotificationEvent(KEY_REGISTRATION_UPDATED, actor, obj), getRecipients(actor, obj))
    }

    override fun handleDeleted(actor: Account, obj: Registration) {
        service.process(NotificationEvent(KEY_REGISTRATION_DELETED, actor, obj), getRecipients(actor, obj))
    }


    private fun getRecipients(actor: Account, registration: Registration): Collection<Account> {
        val recipients = mutableSetOf<Account>()
        val event = eventService.get(registration.eventId)
        if (event != null && actor.id != event.owner.id) {
            recipients.add(event.owner)
        }
        val participants = registrationService.getParticipants(registration.id)
        participants.forEach { recipients.add(it.author) }
        return recipients
    }

    override fun participantChanged(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    ) {
        val event = eventService.get(registration.eventId)
            ?: return logger.error("Cannot find event for registration ${registration.id}")
        val location =
            eventService.getLocation(event.id) ?: return logger.error("Cannot find location for event ${event.id}")
        val content = RegistrationEventContent(event, registration, participant, location)
        service.process(
            NotificationEvent(
                KEY_PARTICIPANT_CHANGED,
                actor,
                content
            ), listOf(event.owner)
        )

        val eventType = when (status) {
            ParticipateStatus.ACCEPTED -> KEY_PARTICIPANT_ACCEPTED
            ParticipateStatus.DECLINED -> KEY_PARTICIPANT_DECLINED
            ParticipateStatus.WAITING_LIST -> KEY_PARTICIPANT_WAITLIST
            ParticipateStatus.WAITING_LIST_DECREASE_SIZE -> KEY_PARTICIPANT_WAITLIST
            else -> null
        }
        if (eventType == null) {
            logger.error("Unconsidered participant status $status")
            return
        }

        val attachment = createAttachment(event)

        service.process(
            NotificationEvent(
                eventType,
                actor,
                content,
                attachments = listOf(attachment)
            ),
            listOf(participant.author),
        )
    }

    private fun createAttachment(event: Event): Attachment {
        val cal = ICalendar()
        val e = VEvent()
        e.summary = Summary(event.title)

        e.setDateStart(convert(event.start), true)
        e.setDateEnd(convert(event.finish), true)
        cal.addEvent(e)

        val content = Biweekly.write(cal).go().toByteArray(Charset.defaultCharset())
        return Attachment("invitation.ics", content, "text/calendar")
    }

    private fun convert(timestamp: LocalDateTime): Date {
        val offset = ZONE_OFFSET.rules.getOffset(timestamp)
        val instant = timestamp.toInstant(offset)
        return Date.from(instant)
    }

    override fun participantRemoved(actor: Account, registration: Registration, participant: Participant) {
        val event = eventService.get(registration.eventId)
            ?: return logger.error("Cannot find event for registration ${registration.id}")
        val location =
            eventService.getLocation(event.id) ?: return logger.error("Cannot find location for event ${event.id}")
        val content = RegistrationEventContent(event, registration, participant, location)

        service.process(NotificationEvent(KEY_PARTICIPANT_REMOVED, actor, content), listOf(event.owner))

        service.process(
            NotificationEvent(
                KEY_PARTICIPANT_DECLINED,
                actor,
                content
            ), listOf(participant.author)
        )
    }
}
