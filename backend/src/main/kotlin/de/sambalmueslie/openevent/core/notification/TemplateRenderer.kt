package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.core.notification.api.NotificationTemplate
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import jakarta.inject.Singleton
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.StringWriter


@Singleton
class TemplateRenderer {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TemplateRenderer::class.java)
    }

    private val ve = VelocityEngine()

    init {
        ve.init()
    }

    fun <T> render(
        event: NotificationEvent<T>,
        template: NotificationTemplate
    ): Mail {
        val properties = mapOf(
            Pair("actor", event.actor),
            Pair("content", event.content ?: ""),
        )

        val context = VelocityContext(properties)
        val subjectWriter = StringWriter()
        ve.evaluate(context, subjectWriter, event.key, template.subject)
        val subject = subjectWriter.toString()

        val contentWriter = StringWriter()
        ve.evaluate(context, contentWriter, event.key, template.content)
        val plainContent = if (template.plain) contentWriter.toString() else null
        val htmlContent = if (!template.plain) contentWriter.toString() else null
        return Mail(subject = subject, htmlContent, plainContent)
    }

}
