package de.sambalmueslie.openevent.core.activity

import de.sambalmueslie.openevent.api.ActivityAPI
import de.sambalmueslie.openevent.api.ActivityAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.ActivityAPI.Companion.PERMISSION_USER
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityInfo
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.getRealmRoles
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/activity")
@Tag(name = "Activity API")
class ActivityController(
    private val service: ActivityCrudService,
    private val accountService: AccountCrudService,
) : ActivityAPI {


    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Activity? {
        return auth.checkPermission(PERMISSION_USER, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.get(id)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission null
                service.getForAccount(account, id)
            }
        }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Activity> {
        return auth.checkPermission(PERMISSION_USER, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.getAll(pageable)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission Page.empty()
                service.getRecentForAccount(account, pageable)
            }

        }
    }

    @Get("recent")
    override fun getRecentInfos(auth: Authentication, pageable: Pageable): Page<ActivityInfo> {
        return auth.checkPermission(PERMISSION_USER, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission Page.empty()
            service.getRecentInfosForAccount(account, pageable)
        }
    }

    @Get("unread")
    override fun getUnreadInfos(auth: Authentication): List<ActivityInfo> {
        return auth.checkPermission(PERMISSION_USER, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission emptyList()
            service.getUnreadInfosForAccount(account)
        }
    }

    @Put("{id}/read")
    override fun markRead(auth: Authentication, id: Long): Activity? {
        return auth.checkPermission(PERMISSION_USER, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.markRead(account, id)
        }
    }

    @Put("read")
    override fun markReadAll(auth: Authentication): List<ActivityInfo> {
        return auth.checkPermission(PERMISSION_USER, PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission emptyList()
            service.markReadAll(account)
        }
    }

    private fun isAdmin(auth: Authentication) = auth.getRealmRoles().contains(PERMISSION_ADMIN)
}