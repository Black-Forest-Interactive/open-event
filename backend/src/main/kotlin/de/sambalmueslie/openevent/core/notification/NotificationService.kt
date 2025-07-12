package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.api.SettingsAPI
import de.sambalmueslie.openevent.common.PageSequence
import de.sambalmueslie.openevent.common.PageableSequence
import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.notification.api.NotificationScheme
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import de.sambalmueslie.openevent.infrastructure.mail.api.MailSender
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

@Singleton
class NotificationService(
    private val profileService: ProfileCrudService,
    private val typeService: NotificationTypeCrudService,
    private val templateService: NotificationTemplateCrudService,
    private val schemeService: NotificationSchemeCrudService,
    private val settingsService: SettingsService,
    private val renderer: TemplateRenderer,
    private val sender: MailSender
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)
    }

    fun <T> process(event: NotificationEvent<T>, additionalRecipients: Collection<AccountInfo>) {
        val key = event.key
        val type = typeService.findByKey(key)
            ?: return logger.warn("Cannot find definition by $key")
        logger.debug("[${event.key}] found type ${type.id} / ${type.key}")
        val template = templateService.find(type, Locale.GERMAN)
            ?: return logger.warn("Cannot find template for type ${type.key}")
        logger.debug("[${event.key}] found template ${template.id}")
        val mail = renderer.render(event, template)
        event.attachments.forEach { mail.attachments.add(it) }

        notify(event, mail, additionalRecipients)

        val schemes = PageableSequence { schemeService.getByType(type, it) }
        schemes.forEach { processScheme(event, mail, it) }
    }

    private fun <T> processScheme(event: NotificationEvent<T>, mail: Mail, scheme: NotificationScheme) {
        val subscriber = PageSequence { schemeService.getSubscriber(scheme, it) }
        subscriber.forEach { notify(event, mail, it.content) }
    }


    private fun <T> notify(event: NotificationEvent<T>, mail: Mail, recipients: Collection<AccountInfo>) {
        if (recipients.isEmpty()) return logger.debug("[${event.key}] no recipients found - aborting")
        val to = recipients.filter { it.email.isNotBlank() }.map { it.toParticipant() }
        val adminEmail = settingsService.findByKey(SettingsAPI.SETTINGS_MAIL_DEFAULT_ADMIN_ADDRESS)?.value as? String
        val bcc = if (adminEmail != null) listOf(MailParticipant("", adminEmail)) else emptyList()
        if (event.useActorAsSender) {
            val actorProfile = profileService.getForAccount(event.actor)
            if (actorProfile != null) {
                sender.send(mail, actorProfile.toParticipant(), to, bcc, single = true)
            } else {
                sender.send(mail, to, bcc, single = true)
            }
        } else {
            sender.send(mail, to, bcc, single = true)
        }
    }

}

private fun AccountInfo.toParticipant(): MailParticipant {
    return MailParticipant(getTitle(), email)
}

private fun Profile.toParticipant(): MailParticipant {
    return MailParticipant(getTitle(), email ?: "")
}
