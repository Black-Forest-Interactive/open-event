package de.sambalmueslie.openevent.gateway.backoffice.export


import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.export.ExportService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag


@Controller("/api/backoffice/export")
@Tag(name = "Export API")
class ExportController(
    private val service: ExportService,
    private val accountService: AccountCrudService,
) {
    companion object {
        private const val PERMISSION_ADMIN = "export.admin"
    }

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Post("/event/pdf")
    fun exportEventsPdf(auth: Authentication, @Body request: EventSearchRequest): SystemFile? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventsPdf(account, request)
        }
    }

    @Post("/event/pdf")
    fun exportEventsPdfToEmail(auth: Authentication, @Body request: EventSearchRequest): HttpStatus {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission HttpStatus.BAD_REQUEST
            service.exportEventsPdfToEmail(account, request)
            HttpStatus.CREATED
        }
    }


    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Get("/event/{eventId}/pdf")
    fun exportEventPdf(auth: Authentication, eventId: Long): SystemFile? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventPdf(eventId, account)
        }
    }

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Post("/event/summary")
    fun exportEventSummaryExcel(auth: Authentication, @Body request: EventSearchRequest): SystemFile? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventSummaryExcel(account, request)
        }
    }
}
