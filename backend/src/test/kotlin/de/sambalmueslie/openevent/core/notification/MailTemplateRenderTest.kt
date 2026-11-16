package de.sambalmueslie.openevent.core.notification

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.event.contact.EventContact
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplate
import de.sambalmueslie.openevent.core.notification.handler.EventReminderContent
import de.sambalmueslie.openevent.core.notification.handler.RegistrationEventContent
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantCancelRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipantStatus
import de.sambalmueslie.openevent.core.registration.api.Registration
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.io.File
import java.time.LocalDateTime

/**
 * Renders the mail templates from the repository's `templates/` directory to make sure the velocity syntax is valid.
 */
class MailTemplateRenderTest {

    private val renderer = TemplateRenderer()
    private val now = LocalDateTime.of(2026, 3, 10, 18, 0)
    private val owner = AccountInfo(1, "owner", "", "owner@test.de", "Olaf", "Owner", "de")
    private val author = AccountInfo(2, "user", "", "user@test.de", "Paula", "Participant", "de")
    private val event = Event(1, owner, now, now.plusHours(2), "Sommerfest", "", "", "", "", false, true, true, EventStatus.ACTIVE, true, emptySet(), now, null)
    private val registration = Registration(1, 1, 10, false, false)
    private val participant = Participant(1, 2, "", ParticipantStatus.ACCEPTED, 0, false, author, now)
    private val location = Location(1, 1, "Hauptstraße", "1", "12345", "Musterstadt", "DE", "", 0.0, 0.0, 0)

    private fun render(file: String, content: Any): String {
        val template = NotificationTemplate(1, "subject", "de", File("../templates/$file").readText(), false)
        return renderer.render(NotificationEvent("test", mockk<Account>(relaxed = true), content), template).htmlText!!
    }

    @Test
    fun reminderContainsContactAndLocation() {
        val html = render("event.reminder.html", EventReminderContent(event, registration, participant, location, EventContact(1, "0761 1234", "")))
        assertTrue(html.contains("Sommerfest"))
        assertTrue(html.contains("Hauptstraße 1, 12345 Musterstadt"))
        assertTrue(html.contains("tel:0761 1234"))
        assertFalse(html.contains(">Handy<"))
        assertFalse(html.contains("\$content"))
    }

    @Test
    fun reminderWithoutLocationAndContact() {
        val html = render("event.reminder.html", EventReminderContent(event, registration, participant, null, null))
        assertFalse(html.contains(">Addresse<"))
        assertFalse(html.contains(">Telefon<"))
        assertFalse(html.contains("\$content"))
    }

    @Test
    fun removedContainsEscapedCancelMessage() {
        val cancel = ParticipantCancelRequest("Leider krank <b>sorry</b>\nBis bald")
        val html = render("registration.participant.removed.html", RegistrationEventContent(event, registration, participant, location, cancel))
        assertTrue(html.contains("Leider krank &lt;b&gt;sorry&lt;/b&gt;<br>Bis bald"))
    }

    @Test
    fun removedWithoutCancelMessage() {
        val html = render("registration.participant.removed.html", RegistrationEventContent(event, registration, participant, location))
        assertFalse(html.contains(">Nachricht<"))
    }

    @Test
    fun userProvidedValuesAreEscaped() {
        val evilEvent = event.copy(title = "<a href=\"https://evil.test\">Klick</a> & mehr")
        val evilParticipant = participant.copy(author = author.copy(firstName = "<b>Paula</b>"))
        val html = render("event.reminder.html", EventReminderContent(evilEvent, registration, evilParticipant, location, null))
        assertFalse(html.contains("<a href=\"https://evil.test\">"))
        assertTrue(html.contains("&lt;a href=&quot;https://evil.test&quot;&gt;Klick&lt;/a&gt; &amp; mehr"))
        assertFalse(html.contains("<b>Paula</b>"))
        assertTrue(html.contains("&lt;b&gt;Paula&lt;/b&gt;"))
    }

    @Test
    fun plainTemplateIsNotEscapedAndSubjectIsSingleLine() {
        val template = NotificationTemplate(1, "Erinnerung:\n\${content.event.title}", "de", "Titel: \${content.event.title}", true)
        val evilEvent = event.copy(title = "Tom & Jerry")
        val mail = renderer.render(NotificationEvent("test", mockk<Account>(relaxed = true), EventReminderContent(evilEvent, registration, participant, null, null)), template)
        assertEquals("Erinnerung: Tom & Jerry", mail.subject)
        assertEquals("Titel: Tom & Jerry", mail.plainText)
    }
}
