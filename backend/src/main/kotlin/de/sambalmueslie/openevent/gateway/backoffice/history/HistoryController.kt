package de.sambalmueslie.openevent.gateway.backoffice.history

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.history.HistoryCrudService
import de.sambalmueslie.openevent.core.history.api.HistoryEntry
import de.sambalmueslie.openevent.core.history.api.HistoryEventInfo
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/history")
@Tag(name = "BACKOFFICE History API")
class HistoryController(private val service: HistoryCrudService) {

    companion object {
        private const val PERMISSION_READ = "history.read"
        private const val PERMISSION_WRITE = "history.write"
        private const val PERMISSION_ADMIN = "history.admin"
    }

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): HistoryEntry? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.get(id)
        }
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Page<HistoryEntry> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getAll(pageable)
        }
    }

    @Get("/for/event/{eventId}")
    fun getForEvent(auth: Authentication, eventId: Long, pageable: Pageable): Page<HistoryEntry> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getForEvent(eventId, pageable)
        }
    }

    @Get("/info")
    fun getInfos(auth: Authentication, pageable: Pageable): Page<HistoryEventInfo> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getAllInfos(pageable)
        }
    }
}