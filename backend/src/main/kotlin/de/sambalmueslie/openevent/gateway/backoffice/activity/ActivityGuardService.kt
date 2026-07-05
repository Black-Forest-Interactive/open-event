package de.sambalmueslie.openevent.gateway.backoffice.activity

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityCleanupRequest
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class ActivityGuardService(
    private val accountService: AccountCrudService,
    private val service: ActivityCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_ADMIN = "activity.admin"
    }

    private val logger = audit.getLogger("BACKOFFICE Activity API")

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun cleanup(auth: Authentication, request: ActivityCleanupRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission
            service.cleanup(account, request)
        }

    fun getRecentForAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Activity> =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
            service.getRecentForAccount(account, pageable)
        }
}
