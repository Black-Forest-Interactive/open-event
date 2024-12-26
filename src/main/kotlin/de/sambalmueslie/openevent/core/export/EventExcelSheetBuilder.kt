package de.sambalmueslie.openevent.core.export


import de.sambalmueslie.openevent.core.event.api.EventInfo
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.FillPatternType
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.xssf.usermodel.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.awt.Color
import java.time.format.DateTimeFormatter
import java.util.*


class EventExcelSheetBuilder(
    private val wb: XSSFWorkbook,
    private val infos: List<EventInfo>
) {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventExcelSheetBuilder::class.java)

        private val dateFormatter = DateTimeFormatter.ofPattern("EEEE ,dd. LLLL yyyy").withLocale(Locale.GERMAN)
        private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm").withLocale(Locale.GERMAN)

    }

    private val sheet = wb.createSheet("Summary")
    private val styleHeader = wb.createCellStyle()
    private val styleContent = wb.createCellStyle()
    private var rowIndex = 0

    private val styleOfferHeaderBoldCombined = wb.createCellStyle()
    private val styleOfferHeaderText = wb.createCellStyle()
    private val styleBookingCombined = wb.createCellStyle()

    private val boldFont = wb.createFont()
    private val boldFontInverted = wb.createFont()
    private val normalFont = wb.createFont()
    private val colorMap: IndexedColorMap = wb.stylesSource.indexedColors

    fun build() {
        setupStyles()
        setupSheet()
        addHeader()
        addEvents()
    }

    private fun addHeader() {
        val row = sheet.createRow(rowIndex++)
        var c = 0

        val cTitle = row.createHeaderCell(c++)
        cTitle.setCellValue("Title")

        val cDate = row.createHeaderCell(c++)
        cDate.setCellValue("Date")

        val cTime = row.createHeaderCell(c++)
        cTime.setCellValue("Time")

        val cOwner = row.createHeaderCell(c++)
        cOwner.setCellValue("Owner")

        val cLocation = row.createHeaderCell(c++)
        cLocation.setCellValue("Location")

        val cMaxGuest = row.createHeaderCell(c++)
        cMaxGuest.setCellValue("Max Guest")

        val cAccepted = row.createHeaderCell(c++)
        cAccepted.setCellValue("Accepted")

        val cWaitlist = row.createHeaderCell(c)
        cWaitlist.setCellValue("WaitList")
    }

    private fun XSSFRow.createHeaderCell(index: Int): XSSFCell {
        val cell = createCell(index)
        cell.cellStyle = styleHeader
        return cell
    }

    private fun addEvents() {

        infos.forEach { addEvent(it) }
    }

    private fun addEvent(info: EventInfo) {
        val row = sheet.createRow(rowIndex++)

        var c = 0

        val event = info.event


        val cTitle = row.createContentCell(c++)
        cTitle.setCellValue(event.title)

        val cDate = row.createContentCell(c++)
        cDate.setCellValue(event.start.format(dateFormatter))

        val cTime = row.createContentCell(c++)
        cTime.setCellValue("${event.start.format(timeFormatter)} - ${event.finish.format(timeFormatter)}")

        val cOwner = row.createContentCell(c++)
        cOwner.setCellValue(event.owner.name)

        val cLocation = row.createContentCell(c++)
        cLocation.setCellValue(info.location?.format() ?: "")

        val cMaxGuest = row.createContentCell(c++)
        val cAccepted = row.createContentCell(c++)
        val cWaitlist = row.createContentCell(c)

        val registration = info.registration
        if (registration == null) {
            cMaxGuest.setCellValue("")
            cAccepted.setCellValue("")
            cWaitlist.setCellValue("")
        } else {
            cMaxGuest.setCellValue(registration.registration.maxGuestAmount.toString())
            var accepted = 0L
            var waitList = 0L

            registration.participants.forEach { p -> if (p.waitingList) waitList += p.size else accepted += p.size }

            cAccepted.setCellValue(accepted.toString())
            cWaitlist.setCellValue(waitList.toString())
        }

    }

    private fun XSSFRow.createContentCell(index: Int): XSSFCell {
        val cell = createCell(index)
        cell.cellStyle = styleContent
        return cell
    }

    private fun setupStyles() {
        boldFont.bold = true
        boldFont.fontHeightInPoints = 12

        boldFontInverted.bold = true
        boldFontInverted.fontHeightInPoints = 12
        boldFontInverted.setColor(XSSFColor(Color(255, 255, 255), colorMap))

        normalFont.fontHeightInPoints = 12

        styleHeader.setFillForegroundColor(XSSFColor(Color(217, 217, 217), colorMap))
        styleHeader.fillPattern = FillPatternType.SOLID_FOREGROUND
        styleHeader.setFont(boldFont)
        styleHeader.borderBottom = BorderStyle.THIN
        styleHeader.alignment = HorizontalAlignment.CENTER

        styleOfferHeaderBoldCombined.setFillForegroundColor(XSSFColor(Color(217, 217, 217), colorMap))
        styleOfferHeaderBoldCombined.fillPattern = FillPatternType.SOLID_FOREGROUND
        styleOfferHeaderBoldCombined.setFont(boldFont)
        styleOfferHeaderBoldCombined.borderBottom = BorderStyle.THIN
        styleOfferHeaderBoldCombined.wrapText = true
        styleOfferHeaderBoldCombined.verticalAlignment = VerticalAlignment.CENTER
        styleOfferHeaderBoldCombined.alignment = HorizontalAlignment.CENTER

        styleOfferHeaderText.setFillForegroundColor(XSSFColor(Color(217, 217, 217), colorMap))
        styleOfferHeaderText.fillPattern = FillPatternType.SOLID_FOREGROUND
        styleOfferHeaderText.alignment = HorizontalAlignment.RIGHT
        styleOfferHeaderText.borderBottom = BorderStyle.THIN
        styleOfferHeaderText.setFont(normalFont)

        styleContent.fillPattern = FillPatternType.NO_FILL
        styleContent.borderBottom = BorderStyle.THIN
        styleContent.verticalAlignment = VerticalAlignment.CENTER
        styleContent.alignment = HorizontalAlignment.CENTER
        styleContent.setFont(normalFont)

        styleBookingCombined.fillPattern = FillPatternType.NO_FILL
        styleBookingCombined.borderBottom = BorderStyle.THIN
        styleBookingCombined.wrapText = true
        styleBookingCombined.verticalAlignment = VerticalAlignment.CENTER
        styleBookingCombined.alignment = HorizontalAlignment.CENTER
        styleBookingCombined.setFont(normalFont)

    }

    private fun setupSheet() {
        sheet.setColumnWidth(0, 40 * 256)
        sheet.setColumnWidth(1, 30 * 256)
        sheet.setColumnWidth(2, 15 * 256)
        sheet.setColumnWidth(3, 15 * 256)
        sheet.setColumnWidth(4, 40 * 256)
        sheet.setColumnWidth(5, 11 * 256)
        sheet.setColumnWidth(6, 11 * 256)
        sheet.setColumnWidth(7, 11 * 256)
    }
}



