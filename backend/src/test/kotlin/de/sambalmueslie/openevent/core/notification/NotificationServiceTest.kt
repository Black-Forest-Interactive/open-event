package de.sambalmueslie.openevent.core.notification

import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplate
import de.sambalmueslie.openevent.core.notification.api.NotificationType
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailSender
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import io.micronaut.data.model.Page
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.Test
import java.util.*

class NotificationServiceTest {

    private val typeService = mockk<NotificationTypeCrudService>(relaxed = true)
    private val templateService = mockk<NotificationTemplateCrudService>(relaxed = true)
    private val schemeService = mockk<NotificationSchemeCrudService>(relaxed = true)
    private val renderer = mockk<TemplateRenderer>(relaxed = true)
    private val sender = mockk<MailSender>(relaxed = true)

    private val service = NotificationService(
        mockk<ProfileCrudService>(relaxed = true), typeService, templateService, schemeService,
        mockk<SettingsService>(relaxed = true), renderer, sender
    )

    private val recipient = AccountInfo(1, "user", "", "user@test.de", "First", "Last", "de")
    private val event = NotificationEvent("test.key", mockk<Account>(relaxed = true), "content")

    private fun setup(enabled: Boolean) {
        every { typeService.findByKey("test.key") } returns NotificationType(1, "test.key", "Test", "", enabled)
        every { templateService.find(any(), Locale.GERMAN) } returns NotificationTemplate(1, "subject", "de", "content", false)
        every { renderer.render(any<NotificationEvent<String>>(), any()) } returns Mail("subject", "content", null)
        every { schemeService.getByType(any(), any()) } returns Page.empty()
    }

    @Test
    fun sendsMailWhenTypeEnabled() {
        setup(true)
        service.process(event, listOf(recipient))
        verify(exactly = 1) { sender.send(any<Mail>(), any<List<MailParticipant>>(), any<List<MailParticipant>>(), any<Boolean>()) }
    }

    @Test
    fun skipsMailWhenTypeDisabled() {
        setup(false)
        service.process(event, listOf(recipient))
        verify(exactly = 0) { templateService.find(any(), any()) }
        verify(exactly = 0) { sender.send(any<Mail>(), any<List<MailParticipant>>(), any<List<MailParticipant>>(), any<Boolean>()) }
    }
}
