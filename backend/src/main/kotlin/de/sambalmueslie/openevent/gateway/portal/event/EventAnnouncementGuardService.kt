package de.sambalmueslie.openevent.gateway.portal.event

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.db.EventAnnouncementRelationService
import de.sambalmueslie.openevent.error.IllegalAccessException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class EventAnnouncementGuardService(
    private val eventCrudService: EventCrudService,
    private val announcementCrudService: AnnouncementCrudService,
    private val announcementRelationService: EventAnnouncementRelationService,
    private val accountService: AccountCrudService,
) {

    companion object {
        private const val PERMISSION_READ = "event.read"
        private const val PERMISSION_WRITE = "event.write"
    }

    fun getAnnouncements(auth: Authentication, eventId: Long, pageable: Pageable): Page<Announcement> =
        auth.checkPermission(PERMISSION_READ) {
            val event = eventCrudService.get(eventId) ?: return@checkPermission Page.empty()
            if (!event.published) return@checkPermission Page.empty()
            val account = accountService.find(auth)
            if (event.owner.id != account.id) throw IllegalAccessException("Only the event owner can send announcements")
            announcementRelationService.get(event, pageable)
        }

    fun createAnnouncement(auth: Authentication, eventId: Long, request: AnnouncementChangeRequest) =
        auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.find(auth)
            val event = eventCrudService.get(eventId) ?: throw IllegalAccessException("Event not found")
            if (event.owner.id != account.id) throw IllegalAccessException("Only the event owner can send announcements")
            val announcement = announcementCrudService.create(account, request)
            announcementRelationService.assign(event, announcement)
            announcement
        }
}
