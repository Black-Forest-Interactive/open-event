package de.sambalmueslie.openevent.core.logic.export

import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.export.EventCardPdfExporter
import de.sambalmueslie.openevent.core.link.LinkCrudService
import de.sambalmueslie.openevent.core.link.api.Link
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

class EventCardPdfExportTest {

    @Test
    fun cardSheetContainsTenCardsOnSinglePage() {
        val settingsService = mockk<SettingsService>()
        every { settingsService.findByKey(any()) } returns null
        every { settingsService.getPublicEventListKey() } returns "public-list"
        every { settingsService.getShareUrl() } returns "https://example.com"
        val linkService = mockk<LinkCrudService>()
        every { linkService.findByKey("public-list") } returns
                Link("l1", "public-list", true, emptyMap(), LocalDateTime.of(2026, 7, 1, 8, 0), null)
        val timeProvider = mockk<TimeProvider>()
        every { timeProvider.now() } returns LocalDateTime.of(2026, 7, 11, 12, 0)

        val exporter = EventCardPdfExporter(settingsService, linkService, timeProvider)

        val owner = AccountInfo(1, "orga", "", "info@example.com", "Anna", "Beispiel", "de")
        val event = Event(
            1, owner,
            LocalDateTime.of(2026, 8, 1, 14, 0), LocalDateTime.of(2026, 8, 1, 17, 0),
            "Kinderbasteln",
            "<p>Ein betreutes Bastelprogramm für Kinder von 4 bis 10 Jahren. Material ist vorhanden, Eltern können in der Zeit andere Veranstaltungen besuchen.</p>",
            "<ul><li>Färorische Fischsuppe genießen 🎉</li><li>mit Bildern eintauchen</li></ul>",
            "", "", false, true, true, true, setOf("Sommer"), LocalDateTime.of(2026, 7, 1, 8, 0), null
        )
        val location = Location(1, 1, "Musterweg", "5", "12345", "Musterstadt", "DE", "", 0.0, 0.0, 20)
        val registration = RegistrationInfo(Registration(1, 1, 25, true, false), emptyList())
        val info = EventInfo(event, location, registration, listOf(Audience(1, "Familien", "")), listOf(Category(1, "Kinder", "")), null, false, false)

        val file = exporter.exportEvent(info)
        assertNotNull(file)

        val (pageCount, text) = Loader.loadPDF(file!!.file).use { it.numberOfPages to PDFTextStripper().getText(it) }
        assertEquals(1, pageCount, "expected a single card sheet page")
        val cardCount = Regex("Kinderbasteln").findAll(text).count()
        assertEquals(10, cardCount, "expected 10 business cards (SIGEL LP799 grid), text: $text")
        assertTrue(text.contains("Samstag"), "weekday missing in: $text")
        assertTrue(text.contains("Familien"), "audience missing in: $text")
        assertTrue(text.contains("Sommer"), "tag missing in: $text")
    }
}
