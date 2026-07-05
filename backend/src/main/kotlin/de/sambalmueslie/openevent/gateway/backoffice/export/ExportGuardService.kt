package de.sambalmueslie.openevent.gateway.backoffice.export

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.export.ExportService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class ExportGuardService(
    private val service: ExportService,
    private val accountService: AccountCrudService,
) {
    companion object {
        private const val PERMISSION_ADMIN = "export.admin"
    }

    fun exportEventsPdf(auth: Authentication, request: EventSearchRequest): SystemFile? =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventsPdf(account, request)
        }

    fun exportEventsPdfToEmail(auth: Authentication, request: EventSearchRequest): HttpStatus =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission HttpStatus.BAD_REQUEST
            service.exportEventsPdfToEmail(account, request)
            HttpStatus.CREATED
        }

    fun exportEventPdf(auth: Authentication, eventId: Long): SystemFile? =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventPdf(eventId, account)
        }

    fun exportNoticePdf(auth: Authentication, request: EventSearchRequest): SystemFile? =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportNoticePdf(account, request)
        }

    fun exportEventSummaryExcel(auth: Authentication, request: EventSearchRequest): SystemFile? =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventSummaryExcel(account, request)
        }
}
