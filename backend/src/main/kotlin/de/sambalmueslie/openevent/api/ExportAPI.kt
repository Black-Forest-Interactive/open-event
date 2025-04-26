package de.sambalmueslie.openevent.api

import io.micronaut.http.HttpStatus
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.security.authentication.Authentication

interface ExportAPI {

    companion object {
        const val PERMISSION_EXPORT = "openevent.export"
    }

    fun exportEventSummaryExcel(auth: Authentication): SystemFile?

    fun exportEventsPdf(auth: Authentication): SystemFile?
    fun exportEventsPdfToEmail(auth: Authentication): HttpStatus
    fun exportEventPdf(auth: Authentication, eventId: Long): SystemFile?

}
