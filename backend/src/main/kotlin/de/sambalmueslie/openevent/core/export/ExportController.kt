package de.sambalmueslie.openevent.core.export


import de.sambalmueslie.openevent.api.ExportAPI
import de.sambalmueslie.openevent.api.ExportAPI.Companion.PERMISSION_EXPORT
import de.sambalmueslie.openevent.core.CoreAPI
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag


@Controller("/api/export")
@Tag(name = "Export API")
@CoreAPI
class ExportController(
    private val service: ExportService,
    private val accountService: AccountCrudService,
) : ExportAPI {

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Get("/event/pdf")
    override fun exportEventsPdf(auth: Authentication): SystemFile? {
        return auth.checkPermission(PERMISSION_EXPORT) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventsPdf(account)
        }
    }

    @Post("/event/pdf")
    override fun exportEventsPdfToEmail(auth: Authentication): HttpStatus {
        return auth.checkPermission(PERMISSION_EXPORT) {
            val account = accountService.get(auth) ?: return@checkPermission HttpStatus.BAD_REQUEST
            service.exportEventsPdfToEmail(account)
            HttpStatus.CREATED
        }
    }


    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Get("/event/{eventId}/pdf")
    override fun exportEventPdf(auth: Authentication, eventId: Long): SystemFile? {
        return auth.checkPermission(PERMISSION_EXPORT) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventPdf(eventId, account)
        }
    }

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Get("/event/summary")
    override fun exportEventSummaryExcel(auth: Authentication): SystemFile? {
        return auth.checkPermission(PERMISSION_EXPORT) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventSummaryExcel(account)
        }
    }
}
