package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.AnnouncementAPI
import de.sambalmueslie.openevent.api.AnnouncementAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.AnnouncementAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.auth.getEmail
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.announcement.AnnouncementCrudService
import de.sambalmueslie.openevent.core.model.Announcement
import de.sambalmueslie.openevent.core.model.AnnouncementChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/announcement")
@Tag(name = "Announcement API")
class AnnouncementController(
    private val service: AnnouncementCrudService,
    private val accountService: AccountCrudService
) : AnnouncementAPI {

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Announcement? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Announcement> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: AnnouncementChangeRequest): Announcement {
        return auth.checkPermission(PERMISSION_WRITE) {
            val author = accountService.findByEmail(auth.getEmail())
                ?: throw InvalidRequestException("Cannot find author account")
            service.create(author, request)
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: AnnouncementChangeRequest): Announcement {
        return auth.checkPermission(PERMISSION_WRITE) { service.update(id, request) }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Announcement? {
        return auth.checkPermission(PERMISSION_WRITE) { service.delete(id) }
    }

}
