package de.sambalmueslie.openevent.core.history


import de.sambalmueslie.openevent.api.HistoryAPI
import de.sambalmueslie.openevent.api.HistoryAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.HistoryAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.getRealmRoles
import de.sambalmueslie.openevent.core.history.api.HistoryEntry
import de.sambalmueslie.openevent.core.history.api.HistoryEventInfo
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/history")
@Tag(name = "History API")
class HistoryController(
    private val service: HistoryCrudService,
    private val migrationService: HistoryMigrationService,
    private val accountService: AccountCrudService,
) : HistoryAPI {

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): HistoryEntry? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.get(id)
        }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<HistoryEntry> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.getAll(pageable)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission Page.empty()
                service.getAllForAccount(account, pageable)
            }
        }
    }

    @Get("/for/event/{eventId}")
    override fun getForEvent(auth: Authentication, eventId: Long, pageable: Pageable): Page<HistoryEntry> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.getForEvent(eventId, pageable)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission Page.empty()
                service.getForEventAndAccount(eventId, account, pageable)
            }
        }
    }

    @Get("/info")
    override fun getInfos(auth: Authentication, pageable: Pageable): Page<HistoryEventInfo> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) {
            if (isAdmin(auth)) {
                service.getAllInfos(pageable)
            } else {
                val account = accountService.get(auth) ?: return@checkPermission Page.empty()
                service.getAllInfosForAccount(account, pageable)
            }
        }
    }

    @Post("/migrate")
    fun migrate(auth: Authentication) {
        return auth.checkPermission(PERMISSION_ADMIN) {
            migrationService.migrate()
        }
    }

    private fun isAdmin(auth: Authentication) = auth.getRealmRoles().contains(PERMISSION_ADMIN)
}
