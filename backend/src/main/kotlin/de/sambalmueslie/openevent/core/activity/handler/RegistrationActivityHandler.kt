package de.sambalmueslie.openevent.core.activity.handler

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.ActivitySourceStorage
import de.sambalmueslie.openevent.core.activity.ActivityTypeStorage
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.db.ActivitySubscriberRelationService
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus
import de.sambalmueslie.openevent.core.registration.RegistrationChangeListener
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.Registration
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class RegistrationActivityHandler(
    private val registrationService: RegistrationCrudService,
    service: ActivityCrudService,
    typeStorage: ActivityTypeStorage,
    sourceStorage: ActivitySourceStorage,
    subscriberService: ActivitySubscriberRelationService,

    private val eventService: EventCrudService,
    private val accountService: AccountCrudService,
) : BaseActivityHandler(service, typeStorage, sourceStorage, subscriberService, logger), RegistrationChangeListener {

    companion object {
        private val logger = LoggerFactory.getLogger(RegistrationActivityHandler::class.java)
        val SOURCE = "registration"
        private val TYPE_PARTICIPANT_ACCEPTED = "registration.participant.accepted"
        private val TYPE_PARTICIPANT_CHANGED = "registration.participant.changed"
        private val TYPE_PARTICIPANT_DECLINED = "registration.participant.declined"
    }

    override fun getSourceKey(): String {
        return SOURCE
    }

    override fun registerListener() {
        registrationService.register(this)
    }

    override fun participantCreated(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    ) {
        val type = when (status) {
            ParticipateStatus.ACCEPTED -> TYPE_PARTICIPANT_ACCEPTED
            ParticipateStatus.WAITING_LIST -> TYPE_PARTICIPANT_ACCEPTED
            ParticipateStatus.WAITING_LIST_DECREASE_SIZE -> TYPE_PARTICIPANT_ACCEPTED
            else -> null
        } ?: return
        createActivity(actor, registration, participant, type)
    }

    override fun participantChanged(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    ) {
        val type = when (status) {
            ParticipateStatus.ACCEPTED -> TYPE_PARTICIPANT_CHANGED
            ParticipateStatus.WAITING_LIST -> TYPE_PARTICIPANT_CHANGED
            ParticipateStatus.WAITING_LIST_DECREASE_SIZE -> TYPE_PARTICIPANT_CHANGED
            else -> null
        } ?: return
        createActivity(actor, registration, participant, type)
    }

    override fun participantRemoved(actor: Account, registration: Registration, participant: Participant) {
        createActivity(actor, registration, participant, TYPE_PARTICIPANT_DECLINED)
    }


    private fun createActivity(actor: Account, registration: Registration, participant: Participant, type: String) {
        val event = eventService.get(registration.eventId) ?: return logger.error("[${registration.id}] cannot find event for registration")
        val request = ActivityChangeRequest("${event.title} - ${participant.author.getTitle()}", event.id)
        val activity = create(actor, request, type) ?: return
        setupSubscribers(actor, event, registration, participant, activity)
    }

    private fun setupSubscribers(actor: Account, event: Event, registration: Registration, participant: Participant, activity: Activity) {
        val accountIds = mutableSetOf(event.owner.id)
        val participants = registrationService.getParticipants(registration.id)
        participants.forEach {accountIds.add(it.author.id)}
        accountIds.remove(actor.id)

        val accounts = accountService.getByIds(accountIds)
        subscribe(actor, activity, accounts)
    }

}