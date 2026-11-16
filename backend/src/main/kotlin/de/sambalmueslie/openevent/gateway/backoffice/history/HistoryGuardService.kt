package de.sambalmueslie.openevent.gateway.backoffice.history

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.history.HistoryCrudService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class HistoryGuardService(
    private val service: HistoryCrudService,
    private val accountService: AccountCrudService,
) {

    companion object {
        private const val PERMISSION_ADMIN = "history.admin"
    }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun getForEvent(auth: Authentication, eventId: Long, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { service.getForEvent(eventId, pageable) }

    fun getInfos(auth: Authentication, search: String, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) {
        val account = accountService.get(auth) ?: return@checkPermission null
        val request = EventSearchRequest(search, null, null, false, false, false, false, false)
        service.getAllInfos(account, request, pageable)
    }
}
