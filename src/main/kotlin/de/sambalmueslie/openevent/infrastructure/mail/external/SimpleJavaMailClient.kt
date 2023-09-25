package de.sambalmueslie.openevent.infrastructure.mail.external

import de.sambalmueslie.openevent.api.SettingsAPI
import de.sambalmueslie.openevent.config.MailConfig
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton
import org.simplejavamail.MailException
import org.simplejavamail.api.mailer.config.TransportStrategy
import org.simplejavamail.email.EmailBuilder
import org.simplejavamail.mailer.MailerBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
@Requirements(
    Requires(notEnv = [Environment.TEST]),
    Requires(property = "mail.enabled", value = "true"),
)
class SimpleJavaMailClient(
    private val config: MailConfig,
    private val settingsService: SettingsService
) : MailClient {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SimpleJavaMailClient::class.java)
    }

    private val mailer = MailerBuilder
        .withSMTPServer(config.server, config.port, config.username, config.password)
        .withTransportStrategy(TransportStrategy.SMTPS)
        .withDebugLogging(false)
        .buildMailer()

    override fun send(
        mail: Mail,
        from: MailParticipant,
        to: List<MailParticipant>,
        bcc: List<MailParticipant>
    ): Boolean {
        if (logger.isDebugEnabled) logger.debug("Send mail '${mail.subject}' to ${to.joinToString { it.address }}")
        val builder = EmailBuilder.startingBlank()
        to.forEach { builder.to(it.name, it.address) }
        bcc.forEach { builder.bcc(it.name, it.address) }
        builder.withReplyTo(getReplyToAddress())

        builder.withSubject(mail.subject)
        builder.from(from.name, from.address)
        mail.plainText?.let { builder.withPlainText(it) }
        mail.htmlText?.let { builder.withHTMLText(it) }
        mail.attachments.forEach { builder.withAttachment(it.name, it.data, it.mimeType) }

        val email = builder.buildEmail()
        try {
            mailer.sendMail(email)
        } catch (e: MailException) {
            logger.error("Failed to send mail '${mail.subject}'", e)
            return false
        }
        return true
    }

    private fun getReplyToAddress(): String {
        val settings = settingsService.findByKey(SettingsAPI.SETTINGS_MAIL_REPLY_TO_ADDRESS)

        val value = settings?.value as? String
        if (value.isNullOrBlank()) return config.replyToAddress

        return value
    }
}
