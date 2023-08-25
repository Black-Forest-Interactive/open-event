package de.sambalmueslie.openevent.core.logic.notification


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.NotificationScheme
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import de.sambalmueslie.openevent.infrastructure.mail.api.MailSender
import de.sambalmueslie.openevent.storage.util.PageSequence
import de.sambalmueslie.openevent.storage.util.PageableSequence
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

@Singleton
class NotificationService(
    private val typeService: NotificationTypeCrudService,
    private val templateService: NotificationTemplateCrudService,
    private val schemeService: NotificationSchemeCrudService,
    private val renderer: TemplateRenderer,
    private val sender: MailSender
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationService::class.java)
    }

    fun <T> process(event: NotificationEvent<T>, additionalRecipients: Collection<Account>) {
        val key = event.key
        val type = typeService.findByKey(key) ?: return logger.warn("Cannot find definition by $key")
        val template =
            templateService.find(type, Locale.GERMAN) ?: return logger.warn("Cannot template for type ${type.key}")
        val mail = renderer.render(event, template)

        notify(event, mail, additionalRecipients)

        val schemes = PageableSequence { schemeService.getByType(type, it) }
        schemes.forEach { processScheme(event, mail, it) }
    }

    private fun <T> processScheme(event: NotificationEvent<T>, mail: Mail, scheme: NotificationScheme) {
        val subscriber = PageSequence { schemeService.getSubscriber(scheme, it) }
        subscriber.forEach { notify(event, mail, it.content) }
    }


    private fun <T> notify(event: NotificationEvent<T>, mail: Mail, recipients: Collection<Account>) {
        if (recipients.isEmpty()) return
        if (event.useActorAsSender) {
            sender.send(mail, event.actor.toParticipant(), recipients.map { it.toParticipant() })
        } else {
            sender.send(mail, recipients.map { it.toParticipant() })
        }
    }

}

private fun Account.toParticipant(): MailParticipant {
    return MailParticipant(name, email)
}
