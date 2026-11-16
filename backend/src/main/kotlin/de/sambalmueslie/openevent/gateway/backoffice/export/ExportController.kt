package de.sambalmueslie.openevent.gateway.backoffice.export

import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.MediaType
import io.micronaut.http.annotation.*
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/export")
@Tag(name = "Export API")
class ExportController(private val service: ExportGuardService) {

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Post("/event/pdf")
    fun exportEventsPdf(auth: Authentication, @Body request: EventSearchRequest): SystemFile? = service.exportEventsPdf(auth, request)

    @Post("/event/pdf")
    fun exportEventsPdfToEmail(auth: Authentication, @Body request: EventSearchRequest): HttpStatus = service.exportEventsPdfToEmail(auth, request)

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Get("/event/{eventId}/pdf")
    fun exportEventPdf(auth: Authentication, eventId: Long): SystemFile? = service.exportEventPdf(auth, eventId)

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Post("/event/notice")
    fun exportNoticePdf(auth: Authentication, @Body request: EventSearchRequest): SystemFile? = service.exportNoticePdf(auth, request)

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Post("/event/cards")
    fun exportCardsPdf(auth: Authentication, @Body request: EventSearchRequest): SystemFile? = service.exportCardsPdf(auth, request)

    @Produces(value = [MediaType.APPLICATION_OCTET_STREAM])
    @Post("/event/summary")
    fun exportEventSummaryExcel(auth: Authentication, @Body request: EventSearchRequest): SystemFile? = service.exportEventSummaryExcel(auth, request)
}
