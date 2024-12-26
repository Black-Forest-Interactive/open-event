package de.sambalmueslie.openevent.core.activity.handler

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import de.sambalmueslie.openevent.core.activity.db.ActivitySubscriberRelationService
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus
import de.sambalmueslie.openevent.core.registration.RegistrationChangeListener
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.Registration
import jakarta.inject.Singleton

@Singleton
class RegistrationActivityHandler(
    private val registrationService: RegistrationCrudService,
    private val eventService: EventCrudService,
    private val service: ActivityCrudService,
    private val subscriberService: ActivitySubscriberRelationService
) : ActivityHandler, RegistrationChangeListener {

    override fun setup() {
        registrationService.register(this)
    }

    override fun participantCreated(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    ) {
        val type = when (status) {
            ParticipateStatus.ACCEPTED -> ActivityType.PARTICIPANT_ACCEPTED
            ParticipateStatus.WAITING_LIST -> ActivityType.PARTICIPANT_ACCEPTED
            ParticipateStatus.WAITING_LIST_DECREASE_SIZE -> ActivityType.PARTICIPANT_ACCEPTED
            else -> null
        } ?: return
        participantUpdated(actor, registration, participant, type)
    }

    override fun participantChanged(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    ) {
        val type = when (status) {
            ParticipateStatus.ACCEPTED -> ActivityType.PARTICIPANT_CHANGED
            ParticipateStatus.WAITING_LIST -> ActivityType.PARTICIPANT_CHANGED
            ParticipateStatus.WAITING_LIST_DECREASE_SIZE -> ActivityType.PARTICIPANT_CHANGED
            else -> null
        } ?: return
        participantUpdated(actor, registration, participant, type)
    }

    private fun participantUpdated(
        actor: Account,
        registration: Registration,
        participant: Participant,
        type: ActivityType
    ) {
        val event = eventService.get(registration.eventId) ?: return
        val activity = service.create(actor, createRequest(actor, event, participant, type))
        setupSubscribers(actor, registration, activity, event)
    }


    override fun participantRemoved(actor: Account, registration: Registration, participant: Participant) {
        val event = eventService.get(registration.eventId) ?: return
        val type = ActivityType.PARTICIPANT_DECLINED
        val activity = service.create(actor, createRequest(actor, event, participant, type))
        setupSubscribers(actor, registration, activity, event)
    }

    private fun createRequest(
        actor: Account,
        event: Event,
        participant: Participant,
        type: ActivityType
    ): ActivityChangeRequest {
        val title = "${event.title} - ${participant.author.getTitle()}"
        return ActivityChangeRequest(
            title,
            actor,
            ActivitySource.REGISTRATION,
            type,
            event.id,
        )
    }

    private fun setupSubscribers(actor: Account, registration: Registration, activity: Activity, event: Event) {
        notifyEventOwner(actor, registration, activity)
        notifyOtherParticipants(actor, registration, activity)
    }

    private fun notifyEventOwner(actor: Account, registration: Registration, activity: Activity) {
        val event = eventService.get(registration.eventId) ?: return
        if(actor.id == event.owner.id) return
        subscriberService.addAccountInfoSubscriber(activity, event.owner)
    }

    private fun notifyOtherParticipants(actor: Account, registration: Registration, activity: Activity) {
        val participants = registrationService.getParticipants(registration.id)
        val subscribers = participants.map { it.author }.filter { it.id != actor.id }
        subscriberService.addAccountInfoSubscriber(activity, subscribers)
    }
}