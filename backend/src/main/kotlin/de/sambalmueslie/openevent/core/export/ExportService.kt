package de.sambalmueslie.openevent.core.export


import de.sambalmueslie.openevent.common.PageableSequence
import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.infrastructure.mail.api.Attachment
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import de.sambalmueslie.openevent.infrastructure.mail.api.MailSender
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.scheduling.annotation.Async
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.format.DateTimeFormatter
import java.util.concurrent.atomic.AtomicBoolean

@Singleton
open class ExportService(
    private val eventService: EventCrudService,
    private val searchService: SearchService,
    private val profileService: ProfileCrudService,
    private val excelExporter: EventExcelExporter,
    private val pdfExporter: EventPdfExporter,
    private val mailSender: MailSender,
    private val timeProvider: TimeProvider,
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ExportService::class.java)
    }

    fun exportEventsPdf(account: Account, request: EventSearchRequest): SystemFile? {
        return exportEvents(account, request, pdfExporter)
    }

    fun exportEventPdf(eventId: Long, account: Account): SystemFile? {
        return exportEvent(eventId, account, pdfExporter)
    }

    private val exporting = AtomicBoolean(false)

    @Async
    open fun exportEventsPdfToEmail(account: Account, request: EventSearchRequest) {
        val profile = profileService.getForAccount(account) ?: return
        val email = profile.email ?: return

        if (exporting.get()) return
        exporting.set(true)
        try {
            val result = exportEvents(account, request, pdfExporter) ?: return
            val date = timeProvider.now().toLocalDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"))
            val filename = "${date}-event-export.pdf"
            val attachment = Attachment(filename, result.file.readBytes(), "application/pdf")
            val mail = Mail("Export der Veranstaltungen", null, "", mutableListOf(), mutableListOf(attachment))
            mailSender.send(mail, listOf(MailParticipant(account.name, email)))

        } catch (e: Exception) {
            logger.error("Exception while exporting pdf", e)
        }
        exporting.set(false)
    }

    fun exportEventSummaryExcel(account: Account, request: EventSearchRequest): SystemFile? {
        return exportEvents(account, request, excelExporter)
    }

    private fun exportEvent(eventId: Long, account: Account, exporter: EventExporter): SystemFile? {
        val info = eventService.getInfo(eventId, account) ?: return null
        return exporter.exportEvent(info)
    }

    private fun exportEvents(account: Account, request: EventSearchRequest, exporter: EventExporter): SystemFile? {
        return exporter.exportEvents {
            PageableSequence {
                val result = searchService.searchEvents(account, request, it)
                val eventIds = result.result.content.mapNotNull { e -> e.id.toLongOrNull() }.toSet()
                val infos = eventService.getInfoByIds(eventIds)
                Page.of(infos, result.result.pageable, result.result.totalSize)
            }
        }
    }
}
