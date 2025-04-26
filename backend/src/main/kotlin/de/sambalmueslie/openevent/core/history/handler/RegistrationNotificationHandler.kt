package de.sambalmueslie.openevent.core.history.handler


import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.history.HistoryCrudService
import de.sambalmueslie.openevent.core.history.api.HistoryEntryChangeRequest
import de.sambalmueslie.openevent.core.history.api.HistoryEntrySource
import de.sambalmueslie.openevent.core.history.api.HistoryEntryType
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus
import de.sambalmueslie.openevent.core.registration.RegistrationChangeListener
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.Registration
import io.micronaut.context.annotation.Context
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Context
class RegistrationNotificationHandler(
    registrationService: RegistrationCrudService,
    private val service: HistoryCrudService,
    private val eventService: EventCrudService,
) : RegistrationChangeListener {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RegistrationNotificationHandler::class.java)
        const val KEY_PARTICIPANT_UPDATED = "participant.update"
        const val KEY_PARTICIPANT_REMOVED = "participant.remove"
    }

    init {
        registrationService.register(this)
    }

    override fun participantCreated(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    ) {
        val event = eventService.get(registration.eventId)
            ?: return logger.error("Cannot find event for registration ${registration.id}")

        val request = HistoryEntryChangeRequest(
            HistoryEntryType.PARTICIPANT_STATUS_CHANGED,
            KEY_PARTICIPANT_UPDATED,
            HistoryEntrySource.REGISTRATION,
            status.toString()
        )
        service.create(actor, event, request)
    }

    override fun participantChanged(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    ) {
        val event = eventService.get(registration.eventId)
            ?: return logger.error("Cannot find event for registration ${registration.id}")

        val request = HistoryEntryChangeRequest(
            HistoryEntryType.PARTICIPANT_STATUS_CHANGED,
            KEY_PARTICIPANT_UPDATED,
            HistoryEntrySource.REGISTRATION,
            status.toString()
        )
        service.create(actor, event, request)
    }

    override fun participantRemoved(actor: Account, registration: Registration, participant: Participant) {
        val event = eventService.get(registration.eventId)
            ?: return logger.error("Cannot find event for registration ${registration.id}")

        val request = HistoryEntryChangeRequest(
            HistoryEntryType.PARTICIPANT_STATUS_CHANGED,
            KEY_PARTICIPANT_REMOVED,
            HistoryEntrySource.REGISTRATION,
            ""
        )
        service.create(actor, event, request)
    }


}
