package de.sambalmueslie.openevent.gateway.portal.event

import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/portal/event/{eventId}/announcement")
@Tag(name = "APP Event Announcement API")
class EventAnnouncementController(private val service: EventAnnouncementGuardService) {

    @Get
    fun getAnnouncements(auth: Authentication, eventId: Long, pageable: Pageable) = service.getAnnouncements(auth, eventId, pageable)

    @Post
    fun createAnnouncement(auth: Authentication, eventId: Long, @Body request: AnnouncementChangeRequest) = service.createAnnouncement(auth, eventId, request)
}
