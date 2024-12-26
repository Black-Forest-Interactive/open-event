package de.sambalmueslie.openevent.core.announcement


import de.sambalmueslie.openevent.api.AnnouncementAPI
import de.sambalmueslie.openevent.api.AnnouncementAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.AnnouncementAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.api.AnnouncementChangeRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/announcement")
@Tag(name = "Announcement API")
class AnnouncementController(
    private val service: AnnouncementCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : AnnouncementAPI {
    private val logger = audit.getLogger("Announcement API")

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
            logger.traceCreate(auth, request) { service.create(accountService.find(auth), request) }
        }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: AnnouncementChangeRequest): Announcement {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceUpdate(auth, request) { service.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Announcement? {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceDelete(auth) { service.delete(accountService.find(auth), id) }
        }
    }

}
