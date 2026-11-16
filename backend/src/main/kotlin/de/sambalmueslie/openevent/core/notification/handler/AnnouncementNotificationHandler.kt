package de.sambalmueslie.openevent.core.notification.handler


import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.announcement.AnnouncementChangeListener
import de.sambalmueslie.openevent.core.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.db.EventAnnouncementRelationService
import de.sambalmueslie.openevent.core.notification.NotificationEvent
import de.sambalmueslie.openevent.core.notification.NotificationService
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AnnouncementNotificationHandler(
    announcementCrudService: AnnouncementCrudService,
    private val service: NotificationService,
    private val eventAnnouncementRelationService: EventAnnouncementRelationService,
    private val eventCrudService: EventCrudService,
    private val registrationCrudService: RegistrationCrudService,
) : NotificationHandler, AnnouncementChangeListener {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AnnouncementNotificationHandler::class.java)
        const val KEY_ANNOUNCEMENT_CREATED = "announcement.create"
        const val KEY_ANNOUNCEMENT_UPDATED = "announcement.update"
        const val KEY_ANNOUNCEMENT_DELETED = "announcement.delete"
    }

    override fun getName(): String = AnnouncementNotificationHandler::class.java.simpleName

    override fun getTypes(): Set<NotificationTypeChangeRequest> {
        return setOf(
            NotificationTypeChangeRequest(KEY_ANNOUNCEMENT_CREATED, "Announcement created", ""),
            NotificationTypeChangeRequest(KEY_ANNOUNCEMENT_UPDATED, "Announcement changed", ""),
            NotificationTypeChangeRequest(KEY_ANNOUNCEMENT_DELETED, "Announcement deleted", "")
        )
    }

    override fun handleCreated(actor: Account, obj: Announcement) {
        val event = getEvent(obj) ?: return
        service.process(
            NotificationEvent(KEY_ANNOUNCEMENT_CREATED, actor, AnnouncementNotificationContent(event, obj)),
            getRecipients(event)
        )
    }


    override fun handleUpdated(actor: Account, obj: Announcement) {
        val event = getEvent(obj) ?: return
        service.process(
            NotificationEvent(KEY_ANNOUNCEMENT_UPDATED, actor, AnnouncementNotificationContent(event, obj)),
            getRecipients(event)
        )
    }

    override fun handleDeleted(actor: Account, obj: Announcement) {
        val event = getEvent(obj) ?: return
        service.process(
            NotificationEvent(KEY_ANNOUNCEMENT_DELETED, actor, AnnouncementNotificationContent(event, obj)),
            getRecipients(event)
        )
    }


    private fun getEvent(obj: Announcement): Event? {
        val eventId = eventAnnouncementRelationService.findEventId(obj.id) ?: return null
        return eventCrudService.get(eventId)
    }

    private fun getRecipients(event: Event): Collection<AccountInfo> {
        val registration = registrationCrudService.findByEvent(event) ?: return emptyList()
        return registrationCrudService.getParticipants(registration.id).map { it.author }
    }
}
