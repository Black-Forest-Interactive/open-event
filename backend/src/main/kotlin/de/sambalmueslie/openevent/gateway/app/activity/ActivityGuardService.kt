package de.sambalmueslie.openevent.gateway.app.activity

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.api.ActivityCleanupRequest
import de.sambalmueslie.openevent.core.activity.api.ActivityInfo
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class ActivityGuardService(
    private val service: ActivityCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "activity.read"
        private const val PERMISSION_WRITE = "activity.write"
    }

    private val logger = audit.getLogger("APP Activity API")

    fun unreadAmount(auth: Authentication): Long =
        auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission 0
            service.countUnreadForAccount(account)
        }

    fun unreadInfo(auth: Authentication): List<ActivityInfo> =
        auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission emptyList()
            service.getUnreadInfosForAccount(account)
        }

    fun getRecentInfos(auth: Authentication, pageable: Pageable): Page<ActivityInfo> =
        auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.getRecentInfosForAccount(account, pageable)
        }

    fun markReadSingle(auth: Authentication, id: Long) =
        auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: return@checkPermission
            service.markReadSingle(account, id)
        }

    fun markReadAll(auth: Authentication) =
        auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: return@checkPermission
            service.markReadAll(account)
        }
}
