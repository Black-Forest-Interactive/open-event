package de.sambalmueslie.openevent.infrastructure.mail.external


import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import de.sambalmueslie.openevent.infrastructure.mail.api.MailParticipant
import io.micronaut.context.annotation.Requirements
import io.micronaut.context.annotation.Requires
import io.micronaut.context.env.Environment
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
@Requirements(
    Requires(notEnv = [Environment.TEST]),
    Requires(property = "mail.enabled", value = "false"),
)
class NopMailClient : MailClient {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NopMailClient::class.java)
    }

    override fun send(
        mail: Mail,
        from: MailParticipant,
        to: List<MailParticipant>,
        bcc: List<MailParticipant>
    ): Boolean {
        if (logger.isDebugEnabled) logger.debug("Send mail '${mail.subject}' to ${to.joinToString { it.address }}")
        return true
    }


}
