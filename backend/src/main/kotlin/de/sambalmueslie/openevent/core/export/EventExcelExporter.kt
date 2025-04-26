package de.sambalmueslie.openevent.core.export


import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.http.server.types.files.SystemFile
import jakarta.inject.Singleton
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

@Singleton
class EventExcelExporter(
    private val timeProvider: TimeProvider
) : EventExporter {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventExcelExporter::class.java)
        private const val HEADER_EXCEL_FILE_SUFIX = ".xlsx"
        private const val HEADER_EXCEL_FILE_PREFIX = "events"
    }

    override fun exportEvents(provider: () -> Sequence<EventInfo>): SystemFile? {
        val wb = XSSFWorkbook()

        val info = provider.invoke().filter { it.event.published }
        EventExcelSheetBuilder(wb, info.toList()).build()

        val file = File.createTempFile(HEADER_EXCEL_FILE_PREFIX, HEADER_EXCEL_FILE_SUFIX)
        wb.write(file.outputStream())
        val date = timeProvider.now().toLocalDate()
        val filename = "${date}-event-export.xlsx"
        return SystemFile(file).attach(filename)
    }

    override fun exportEvent(info: EventInfo): SystemFile? {
        val wb = XSSFWorkbook()

        EventExcelSheetBuilder(wb, listOf(info)).build()

        val file = File.createTempFile(HEADER_EXCEL_FILE_PREFIX, HEADER_EXCEL_FILE_SUFIX)
        wb.write(file.outputStream())
        val date = timeProvider.now().toLocalDate()
        val filename = "${date}-event-export-${info.event.id}.xlsx"
        return SystemFile(file).attach(filename)
    }
}
