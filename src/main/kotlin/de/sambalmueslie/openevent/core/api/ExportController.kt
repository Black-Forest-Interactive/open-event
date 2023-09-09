package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.ExportAPI
import de.sambalmueslie.openevent.api.ExportAPI.Companion.PERMISSION_EXPORT
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.export.ExportService
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Produces
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/export")
@Tag(name = "Export API")
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
    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Get("/event/excel")
    override fun exportEventsExcel(auth: Authentication): SystemFile? {
        return auth.checkPermission(PERMISSION_EXPORT) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventsExcel(account)
        }
    }
    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Get("/event/{eventId}/excel")
    override fun exportEventExcel(auth: Authentication, eventId: Long): SystemFile? {
        return auth.checkPermission(PERMISSION_EXPORT) {
            val account = accountService.get(auth) ?: return@checkPermission null
            service.exportEventExcel(eventId, account)
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



}
