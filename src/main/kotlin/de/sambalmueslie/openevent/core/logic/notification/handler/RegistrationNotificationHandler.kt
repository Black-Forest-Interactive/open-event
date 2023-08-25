package de.sambalmueslie.openevent.core.logic.notification.handler


import de.sambalmueslie.openevent.core.logic.event.EventCrudService
import de.sambalmueslie.openevent.core.logic.notification.NotificationEvent
import de.sambalmueslie.openevent.core.logic.notification.NotificationService
import de.sambalmueslie.openevent.core.logic.registration.RegistrationChangeListener
import de.sambalmueslie.openevent.core.logic.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.model.*
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
        if (event != null) {
            service.process(NotificationEvent(KEY_PARTICIPANT_CHANGED, actor, registration), listOf(event.owner))
        }

        when (status) {
            ParticipateStatus.ACCEPTED -> service.process(
                NotificationEvent(
                    KEY_PARTICIPANT_ACCEPTED,
                    actor,
                    registration
                ), listOf(participant.author)
            )

            ParticipateStatus.DECLINED -> service.process(
                NotificationEvent(
                    KEY_PARTICIPANT_DECLINED,
                    actor,
                    registration
                ), listOf(participant.author)
            )

            ParticipateStatus.WAITING_LIST -> service.process(
                NotificationEvent(
                    KEY_PARTICIPANT_WAITLIST,
                    actor,
                    registration
                ), listOf(participant.author)
            )

            ParticipateStatus.WAITING_LIST_DECREASE_SIZE -> service.process(
                NotificationEvent(
                    KEY_PARTICIPANT_WAITLIST,
                    actor,
                    registration
                ), listOf(participant.author)
            )

            else -> logger.error("Unconsidered participant status $status")
        }
    }

    override fun participantRemoved(actor: Account, registration: Registration, participant: Participant) {
        val event = eventService.get(registration.eventId)
        if (event != null) {
            service.process(NotificationEvent(KEY_PARTICIPANT_REMOVED, actor, registration), listOf(event.owner))
        }
        service.process(
            NotificationEvent(
                KEY_PARTICIPANT_DECLINED,
                actor,
                registration
            ), listOf(participant.author)
        )
    }
}
