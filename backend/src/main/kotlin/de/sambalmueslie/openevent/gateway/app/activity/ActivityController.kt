package de.sambalmueslie.openevent.gateway.app.activity

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.activity.ActivityCrudService
import de.sambalmueslie.openevent.core.activity.api.ActivityInfo
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/activity")
@Tag(name = "APP Activity API")
class ActivityController(
    private val service: ActivityCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "activity.read"
        private const val PERMISSION_WRITE = "activity.write"
    }


    private val logger = audit.getLogger("APP Activity API")

    @Get("unread/amount")
    fun unreadAmount(auth: Authentication): Long {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission 0
            service.countUnreadForAccount(account)
        }
    }

    @Get("unread/info")
    fun unreadInfo(auth: Authentication): List<ActivityInfo> {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission emptyList()
            service.getUnreadInfosForAccount(account)
        }
    }

    @Get("recent")
    fun getRecentInfos(auth: Authentication, pageable: Pageable): Page<ActivityInfo> {
        return auth.checkPermission(PERMISSION_READ) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.getRecentInfosForAccount(account, pageable)
        }
    }

    @Put("read/{id}")
    fun markReadSingle(auth: Authentication, id: Long) {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: return@checkPermission
            service.markReadSingle(account, id)
        }
    }

    @Put("read")
    fun markReadAll(auth: Authentication) {
        return auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: return@checkPermission
            service.markReadAll(account)
        }
    }

}