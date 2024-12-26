package de.sambalmueslie.openevent.core.activity.handler

import de.sambalmueslie.openevent.common.PageSequence
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import de.sambalmueslie.openevent.core.activity.db.ActivitySubscriberRelationService
import de.sambalmueslie.openevent.core.event.EventChangeListener
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import jakarta.inject.Singleton

@Singleton
class EventActivityHandler(
    private val eventService: EventCrudService,
    private val service: ActivityCrudService,
    private val subscriberService: ActivitySubscriberRelationService,
    private val accountService: AccountCrudService,
    private val registrationService: RegistrationCrudService,
) : ActivityHandler, EventChangeListener {


    override fun setup() {
        eventService.register(this)
    }

    override fun handleCreated(actor: Account, obj: Event) {
        val request = ActivityChangeRequest(
            obj.title,
            actor,
            ActivitySource.EVENT,
            ActivityType.EVENT_CREATED,
            obj.id,
        )
        val activity = service.create(actor, request)
        setupSubscribers(actor, obj, activity)
    }


    override fun handleUpdated(actor: Account, obj: Event) {
        val request = ActivityChangeRequest(
            obj.title,
            actor,
            ActivitySource.EVENT,
            ActivityType.EVENT_CHANGED,
            obj.id,
        )
        val activity = service.create(actor, request)
        setupSubscribers(actor, obj, activity)
    }

    override fun handleDeleted(actor: Account, obj: Event) {
        val request = ActivityChangeRequest(
            obj.title,
            actor,
            ActivitySource.EVENT,
            ActivityType.EVENT_DELETED,
            obj.id,

            )
        val activity = service.create(actor, request)
        setupPrivateSubscribers(actor, obj, activity)
    }

    override fun publishedChanged(actor: Account, event: Event) {
        val request = ActivityChangeRequest(
            event.shortText,
            actor,
            ActivitySource.EVENT,
            ActivityType.EVENT_PUBLISHED,
            event.id,
        )
        val activity = service.create(actor, request)
        setupSubscribers(actor, event, activity)
    }

    private fun setupSubscribers(actor: Account, event: Event, activity: Activity) {
        if (event.published) {
            setupPublicSubscribers(actor, event, activity)
        } else {
            setupPrivateSubscribers(actor, event, activity)
        }
    }


    private fun setupPublicSubscribers(actor: Account, event: Event, activity: Activity) {
        val sequence = PageSequence { accountService.getAll(it) }
        sequence.forEach {p -> subscriberService.addAccountSubscriber(activity, p.content.filter { it.id != actor.id }) }
    }

    private fun setupPrivateSubscribers(actor: Account, event: Event, activity: Activity) {
        val subscribers = mutableSetOf<AccountInfo>()
        val changeByOwner = actor.id == event.owner.id
        if (!changeByOwner) {
            subscribers.add(event.owner)
        }
        val registration = registrationService.findByEvent(event)
        if (registration != null) {
            val participants = registrationService.getParticipants(registration.id)
            participants.forEach { subscribers.add(it.author) }
        }
        subscriberService.addAccountInfoSubscriber(activity, subscribers)
        subscriberService.addAccountSubscriber(activity, setOf(actor))
    }
}