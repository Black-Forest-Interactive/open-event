package de.sambalmueslie.openevent.core.logic.notification.handler


import de.sambalmueslie.openevent.core.logic.event.EventChangeListener
import de.sambalmueslie.openevent.core.logic.event.EventCrudService
import de.sambalmueslie.openevent.core.logic.notification.NotificationEvent
import de.sambalmueslie.openevent.core.logic.notification.NotificationService
import de.sambalmueslie.openevent.core.logic.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.NotificationTypeChangeRequest
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventNotificationHandler(
    eventService: EventCrudService,
    private val registrationService: RegistrationCrudService,
    private val service: NotificationService,
) : NotificationHandler, EventChangeListener {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventNotificationHandler::class.java)
        const val KEY_EVENT_CREATED = "event.create"
        const val KEY_EVENT_UPDATED = "event.update"
        const val KEY_EVENT_DELETED = "event.delete"
        const val KEY_EVENT_PUBLISHED = "event.publish"
        const val KEY_EVENT_UNPUBLISHED = "event.unpublish"
    }

    override fun getName(): String = EventNotificationHandler::class.java.simpleName

    override fun getTypes(): Set<NotificationTypeChangeRequest> {
        return setOf(
            NotificationTypeChangeRequest(KEY_EVENT_CREATED, "Event created", ""),
            NotificationTypeChangeRequest(KEY_EVENT_UPDATED, "Event changed", ""),
            NotificationTypeChangeRequest(KEY_EVENT_DELETED, "Event deleted", ""),
            NotificationTypeChangeRequest(KEY_EVENT_PUBLISHED, "Event published", ""),
            NotificationTypeChangeRequest(KEY_EVENT_UNPUBLISHED, "Event unpublished", ""),
        )
    }

    init {
        eventService.register(this)
    }

    override fun handleCreated(actor: Account, obj: Event) {
        service.process(NotificationEvent(KEY_EVENT_CREATED, actor, obj), getRecipients(actor, obj))
    }

    override fun handleUpdated(actor: Account, obj: Event) {
        service.process(NotificationEvent(KEY_EVENT_UPDATED, actor, obj), getRecipients(actor, obj))
    }

    override fun handleDeleted(actor: Account, obj: Event) {
        service.process(NotificationEvent(KEY_EVENT_DELETED, actor, obj), getRecipients(actor, obj))
    }

    override fun publishedChanged(actor: Account, event: Event) {
        if (event.published) {
            service.process(NotificationEvent(KEY_EVENT_PUBLISHED, actor, event), getRecipients(actor, event))
        } else {
            service.process(NotificationEvent(KEY_EVENT_UNPUBLISHED, actor, event), getRecipients(actor, event))
        }
    }

    private fun getRecipients(actor: Account, event: Event): Set<Account> {
        val recipients = mutableSetOf<Account>()
        val changeByOwner = actor.id == event.owner.id
        if (!changeByOwner) {
            recipients.add(event.owner)
        }
        val registration = registrationService.findByEvent(event)
        if (registration != null) {
            val participants = registrationService.getParticipants(registration.id)
            participants.forEach { recipients.add(it.author) }
        }
        return recipients
    }

}
