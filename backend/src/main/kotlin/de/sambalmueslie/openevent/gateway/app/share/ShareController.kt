package de.sambalmueslie.openevent.gateway.app.share

import de.sambalmueslie.openevent.api.ShareAPI
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.ShareCrudService
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest
import de.sambalmueslie.openevent.error.InsufficientPermissionsException
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/share")
@Tag(name = "APP Share API")
class ShareController(
    private val service: ShareCrudService,
    private val eventService: EventCrudService,
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
            val account = accountService.get(auth) ?: return@checkPermission null
            val result = service.findByEvent(eventId) ?: return@checkPermission null
            if (result.owner.id == account.id) result else null
        }
    }


    @Post()
    fun create(auth: Authentication, @Body request: ShareChangeRequest): Share {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (account, event) = validateRequest(auth, request)
            logger.traceCreate(auth, request) { service.create(account, request, event) }
        }
    }

    @Put("/{id}")
    fun update(auth: Authentication, id: String, @Body request: ShareChangeRequest): Share {
        return auth.checkPermission(PERMISSION_WRITE) {
            val (account, share) = validateUpdate(auth, id)
            logger.traceUpdate(auth, request) { service.update(account, id, request) }
        }
    }


    @Put("/{id}/published")
    fun setPublished(auth: Authentication, id: String, @Body value: PatchRequest<Boolean>): Share? {
        return auth.checkPermission(ShareAPI.PERMISSION_WRITE) {
            val (account, share) = validateUpdate(auth, id)
            logger.traceAction(auth, "PUBLISHED", id, value) { service.setPublished(account, id, value) }
        }
    }


    private fun validateRequest(auth: Authentication, request: ShareChangeRequest): Pair<Account, Event> {
        val account = accountService.find(auth)
        val event = eventService.get(request.eventId) ?: throw InvalidRequestException("Cannot find event for share")
        if (event.owner.id != account.id) throw InsufficientPermissionsException("You are not allowed to change that event")
        return account to event
    }

    private fun validateUpdate(auth: Authentication, id: String): Pair<Account, Share> {
        val account = accountService.find(auth)
        val share = service.get(id) ?: throw InvalidRequestException("Cannot find share by id")
        if (share.owner.id != account.id) throw InsufficientPermissionsException("You are not allowed to change that share")
        return account to share
    }

}