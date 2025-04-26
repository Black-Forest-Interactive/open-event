package de.sambalmueslie.openevent.core.export


import de.sambalmueslie.openevent.common.PageableSequence
import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.infrastructure.mail.api.Attachment
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import de.sambalmueslie.openevent.infrastructure.mail.api.MailSender
import io.micronaut.http.server.types.files.SystemFile
import io.micronaut.scheduling.annotation.Async
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.atomic.AtomicBoolean

@Singleton
open class ExportService(
    private val eventService: EventCrudService,
    private val profileService: ProfileCrudService,
    private val excelExporter: EventExcelExporter,
    private val pdfExporter: EventPdfExporter,
    private val mailSender: MailSender
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ExportService::class.java)
    }

    fun exportEventsPdf(account: Account): SystemFile? {
        return exportEvents(account, pdfExporter)
    }

    private fun exportEvents(account: Account, exporter: EventExporter): SystemFile? {
        return exporter.exportEvents { PageableSequence { eventService.getInfos(it) } }
    }

    fun exportEventPdf(eventId: Long, account: Account): SystemFile? {
        return exportEvent(eventId, account, pdfExporter)
    }

    private fun exportEvent(eventId: Long, account: Account, exporter: EventExporter): SystemFile? {
        val info = eventService.getInfo(eventId, account) ?: return null
        return exporter.exportEvent(info)
    }

    private val exporting = AtomicBoolean(false)

    @Async
    open fun exportEventsPdfToEmail(account: Account) {
        val profile = profileService.getForAccount(account) ?: return
        val email = profile.email ?: return

        if (exporting.get()) return
        exporting.set(true)
        try {
            val result = exportEvents(account, pdfExporter) ?: return

            val attachment = Attachment(result.file.name, result.file.readBytes(), result.mediaType.name)
            val mail = Mail("Export der Veranstaltungen", null, "", mutableListOf(), mutableListOf(attachment))
            mailSender.send(mail, listOf(MailParticipant(account.name, email)))

        } catch (e: Exception) {
            logger.error("Exception while exporting pdf", e)
        }
        exporting.set(false)
    }


    fun exportEventSummaryExcel(account: Account): SystemFile? {
        return exportEvents(account, excelExporter)
    }
}
