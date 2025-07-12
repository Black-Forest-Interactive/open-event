package de.sambalmueslie.openevent.gateway.app.share

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.share.ShareCrudService
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest
import de.sambalmueslie.openevent.error.IllegalAccessException
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.gateway.app.event.EventGuardService
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/share")
@Tag(name = "APP Share API")
class ShareController(
    private val service: ShareCrudService,
    private val eventService: EventGuardService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "share.read"
        private const val PERMISSION_WRITE = "share.write"
    }

    private val logger = audit.getLogger("APP Share API")

    @Get("/by/event/{eventId}")
    fun findByEvent(auth: Authentication, eventId: Long): Share? {
        return auth.checkPermission(PERMISSION_READ) {
            val (event, _) = eventService.getIfAccessible(auth, eventId) ?: return@checkPermission null
            service.findByEvent(event.id)
        }
    }


    @Post()
    fun create(auth: Authentication, @Body request: ShareChangeRequest): Share? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (event, actor) = eventService.getIfAccessible(auth, request.eventId) ?: return@checkPermission null
            logger.traceCreate(auth, request) { service.create(actor, request, event) }
        }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: String, @Body request: ShareChangeRequest): Share? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (account, _) = validateUpdate(auth, id)
            logger.traceUpdate(auth, request) { service.update(account, id, request) }
        }
    }


    @Put("/{id}/published")
    fun setPublished(auth: Authentication, id: String, @Body value: PatchRequest<Boolean>): Share? {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (account, _) = validateUpdate(auth, id)
            logger.traceAction(auth, "PUBLISHED", id, value) { service.setPublished(account, id, value) }
        }
    }

    private fun validateUpdate(auth: Authentication, id: String): Pair<Account, Share> {
        val account = accountService.find(auth)
        val share = service.get(id) ?: throw InvalidRequestException("Cannot find share by id")
        if (share.owner.id != account.id) throw IllegalAccessException("You are not allowed to change that share")
        return account to share
    }

}