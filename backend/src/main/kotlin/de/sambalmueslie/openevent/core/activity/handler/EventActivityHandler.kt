package de.sambalmueslie.openevent.core.activity.handler

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.ActivitySourceStorage
import de.sambalmueslie.openevent.core.activity.ActivityTypeStorage
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.db.ActivitySubscriberRelationService
import de.sambalmueslie.openevent.core.event.EventChangeListener
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory

@Singleton
class EventActivityHandler(
    private val eventService: EventCrudService,
    service: ActivityCrudService,
    typeStorage: ActivityTypeStorage,
    sourceStorage: ActivitySourceStorage,
    subscriberService: ActivitySubscriberRelationService,

    private val accountService: AccountCrudService,
    private val registrationService: RegistrationCrudService,
) : BaseActivityHandler(service, typeStorage, sourceStorage, subscriberService, logger), EventChangeListener {

    companion object {
        private val logger = LoggerFactory.getLogger(EventActivityHandler::class.java)
        val SOURCE = "event"
        private val TYPE_CREATED = "event.created"
        private val TYPE_CHANGED = "event.changed"
        private val TYPE_DELETED = "event.deleted"
        private val TYPE_PUBLISHED = "event.published"
    }

    override fun getSourceKey(): String {
        return SOURCE
    }

    override fun registerListener() {
        eventService.register(this)
    }

    override fun handleCreated(actor: Account, obj: Event) {
        createActivity(actor, obj, TYPE_CREATED)
    }

    override fun handleUpdated(actor: Account, obj: Event) {
        createActivity(actor, obj, TYPE_CHANGED)
    }

    override fun handleDeleted(actor: Account, obj: Event) {
        createActivity(actor, obj, TYPE_DELETED)
    }

    override fun publishedChanged(actor: Account, event: Event) {
        createActivity(actor, event, TYPE_PUBLISHED)
    }

    private fun createActivity(actor: Account, event: Event, type: String) {
        val request = ActivityChangeRequest(event.title, event.id)
        val activity = create(actor, request, type) ?: return
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
        subscribe(actor, activity) { accountService.getAll(it) }
    }

    private fun setupPrivateSubscribers(actor: Account, event: Event, activity: Activity) {
        val accountIds = mutableSetOf<Long>()
        val changeByOwner = actor.id == event.owner.id
        if (!changeByOwner) {
            accountIds.add(event.owner.id)
        }
        val registration = registrationService.findByEvent(event)
        if (registration != null) {
            val participants = registrationService.getParticipants(registration.id)
            participants.forEach { accountIds.add(it.author.id) }
        }
        val accounts = accountService.getByIds(accountIds)
        subscribe(actor, activity, accounts)
    }
}