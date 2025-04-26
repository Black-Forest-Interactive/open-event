package de.sambalmueslie.openevent.core.notification.handler


import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.announcement.AnnouncementChangeListener
import de.sambalmueslie.openevent.core.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.notification.NotificationService
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AnnouncementNotificationHandler(
    eventService: AnnouncementCrudService,
    private val service: NotificationService,
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
        service.process(
            de.sambalmueslie.openevent.core.notification.NotificationEvent(
                KEY_ANNOUNCEMENT_CREATED,
                actor,
                obj
            ), getRecipients(actor, obj)
        )
    }


    override fun handleUpdated(actor: Account, obj: Announcement) {
        service.process(
            de.sambalmueslie.openevent.core.notification.NotificationEvent(
                KEY_ANNOUNCEMENT_UPDATED,
                actor,
                obj
            ), getRecipients(actor, obj)
        )
    }

    override fun handleDeleted(actor: Account, obj: Announcement) {
        service.process(
            de.sambalmueslie.openevent.core.notification.NotificationEvent(
                KEY_ANNOUNCEMENT_DELETED,
                actor,
                obj
            ), getRecipients(actor, obj)
        )
    }


    private fun getRecipients(actor: Account, obj: Announcement): Collection<AccountInfo> {
        // get event for announcement
        // get event participants (registered users)
        //        TODO("Not yet implemented")
        return emptyList()
    }
}
