package de.sambalmueslie.openevent.gateway.backoffice.history

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.history.HistoryCrudService
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class HistoryGuardService(private val service: HistoryCrudService) {

    companion object {
        private const val PERMISSION_ADMIN = "history.admin"
    }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun getForEvent(auth: Authentication, eventId: Long, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { service.getForEvent(eventId, pageable) }

    fun getInfos(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAllInfos(pageable) }
}
