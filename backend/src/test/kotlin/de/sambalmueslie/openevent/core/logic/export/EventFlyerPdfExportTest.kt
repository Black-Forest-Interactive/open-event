package de.sambalmueslie.openevent.core.logic.export

import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.export.EventOpenPdfExporter
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.mockk.every
import io.mockk.mockk
import org.apache.pdfbox.Loader
import org.apache.pdfbox.text.PDFTextStripper
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.time.LocalDateTime

class EventFlyerPdfExportTest {

    @Test
    fun flyerContainsTitleShortAndLongText() {
        val settingsService = mockk<SettingsService>()
        every { settingsService.findByKey(any()) } returns null
        val timeProvider = mockk<TimeProvider>()
        every { timeProvider.now() } returns LocalDateTime.of(2026, 7, 11, 12, 0)

        val exporter = EventOpenPdfExporter(settingsService, timeProvider)

        val owner = AccountInfo(1, "orga", "", "info@example.com", "Anna", "Beispiel", "de")
        val event = Event(
            1, owner,
            LocalDateTime.of(2026, 8, 1, 14, 0), LocalDateTime.of(2026, 8, 1, 17, 0),
            "Kinderbasteln",
            "<p>Ein betreutes Bastelprogramm für Kinder von 4 bis 10 Jahren. Material ist vorhanden, Eltern können in der Zeit andere Veranstaltungen besuchen.</p>",
            "<ul><li>Färorische Fischsuppe genießen 🎉</li><li>mit Bildern eintauchen</li></ul>",
            "", "", false, true, true, EventStatus.ACTIVE, true, setOf("Sommer"), LocalDateTime.of(2026, 7, 1, 8, 0), null
        )
        val location = Location(1, 1, "Musterweg", "5", "12345", "Musterstadt", "DE", "", 0.0, 0.0, 20)
        val registration = RegistrationInfo(Registration(1, 1, 25, true, false), emptyList())
        val info = EventInfo(event, location, registration, listOf(Audience(1, "Familien", "")), listOf(Category(1, "Kinder", "")), null, false, false)

        val file = exporter.exportEvent(info)
        assertNotNull(file)

        val (pageCount, text) = Loader.loadPDF(file!!.file).use { it.numberOfPages to PDFTextStripper().getText(it) }
        assertEquals(1, pageCount, "expected a single flyer page")
        assertTrue(text.contains("Kinderbasteln"), "title missing in: $text")
        assertTrue(text.contains("Bastelprogramm"), "short text missing in: $text")
        assertTrue(text.contains("Fischsuppe"), "long text missing in: $text")
        assertTrue(text.contains("Kinder"), "category missing in: $text")
    }
}
