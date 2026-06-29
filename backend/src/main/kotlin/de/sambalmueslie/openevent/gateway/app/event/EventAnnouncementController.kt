package de.sambalmueslie.openevent.gateway.app.event

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
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/event/{eventId}/announcement")
@Tag(name = "APP Event Announcement API")
class EventAnnouncementController(
    private val eventCrudService: EventCrudService,
    private val announcementCrudService: AnnouncementCrudService,
    private val announcementRelationService: EventAnnouncementRelationService,
    private val accountService: AccountCrudService,
) {

    companion object {
        private const val PERMISSION_READ = "event.read"
        private const val PERMISSION_WRITE = "event.write"
    }

    @Get
    fun getAnnouncements(auth: Authentication, eventId: Long, pageable: Pageable): Page<Announcement> {
        return auth.checkPermission(PERMISSION_READ) {
            val event = eventCrudService.get(eventId) ?: return@checkPermission Page.empty()
            announcementRelationService.get(event, pageable)
        }
    }

    @Post
    fun createAnnouncement(auth: Authentication, eventId: Long, @Body request: AnnouncementChangeRequest): Announcement {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.find(auth)
            val event = eventCrudService.get(eventId) ?: throw IllegalAccessException("Event not found")
            if (event.owner.id != account.id) throw IllegalAccessException("Only the event owner can send announcements")
            val announcement = announcementCrudService.create(account, request)
            announcementRelationService.assign(event, announcement)
            announcement
        }
    }
}
