package de.sambalmueslie.openevent.gateway.backoffice.activity

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityCleanupRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/activity")
@Tag(name = "BACKOFFICE Activity API")
class ActivityController(
    private val accountService: AccountCrudService,
    private val service: ActivityCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_ADMIN = "activity.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Activity API")

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Page<Activity> {
        return auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }
    }

    @Get("{id}")
    fun get(auth: Authentication, id: Long): Activity? {
        return auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }
    }

    @Post("cleanup")
    fun cleanup(auth: Authentication, @Body request: ActivityCleanupRequest) {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission
            service.cleanup(account, request)
        }
    }

    @Get("{accountId}/recent")
    fun getRecentForAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Activity> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
            service.getRecentForAccount(account, pageable)
        }
    }
}