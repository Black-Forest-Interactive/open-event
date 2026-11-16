package de.sambalmueslie.openevent.core.notification.handler


import de.sambalmueslie.openevent.core.participant.api.ParticipantCancelRequest
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.notification.NotificationEvent
import de.sambalmueslie.openevent.core.notification.NotificationService
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus
import de.sambalmueslie.openevent.core.registration.RegistrationChangeListener
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.Registration
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

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
        service.process(
            NotificationEvent(
                KEY_REGISTRATION_CREATED,
                actor,
                obj
            ), getRecipients(actor, obj)
        )
    }


    override fun handleUpdated(actor: Account, obj: Registration) {
        service.process(
            NotificationEvent(
                KEY_REGISTRATION_UPDATED,
                actor,
                obj
            ), getRecipients(actor, obj)
        )
    }

    override fun handleDeleted(actor: Account, obj: Registration) {
        service.process(
            NotificationEvent(
                KEY_REGISTRATION_DELETED,
                actor,
                obj
            ), getRecipients(actor, obj)
        )
    }


    private fun getRecipients(actor: Account, registration: Registration): Collection<AccountInfo> {
        val recipients = mutableSetOf<AccountInfo>()
        val event = eventService.get(registration.eventId)
        if (event != null && actor.id != event.owner.id) {
            recipients.add(event.owner)
        }
        val participants = registrationService.getParticipants(registration.id)
        participants.forEach { recipients.add(it.author) }
        return recipients
    }

    override fun participantCreated(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    ) {
        participantChanged(actor, registration, participant, status)
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

        val attachment = EventCalendarAttachment.create(event, location)

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

    override fun participantRemoved(actor: Account, registration: Registration, participant: Participant, cancel: ParticipantCancelRequest?) {
        val event = eventService.get(registration.eventId)
            ?: return logger.error("Cannot find event for registration ${registration.id}")
        val location =
            eventService.getLocation(event.id) ?: return logger.error("Cannot find location for event ${event.id}")
        val content = RegistrationEventContent(event, registration, participant, location, cancel)

        service.process(
            NotificationEvent(
                KEY_PARTICIPANT_REMOVED,
                actor,
                content
            ), listOf(event.owner)
        )

        service.process(
            NotificationEvent(
                KEY_PARTICIPANT_DECLINED,
                actor,
                content
            ), listOf(participant.author)
        )
    }
}
