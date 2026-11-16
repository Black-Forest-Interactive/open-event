package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.core.notification.api.NotificationTemplate
import de.sambalmueslie.openevent.infrastructure.mail.api.Mail
import jakarta.inject.Singleton
import org.apache.velocity.VelocityContext
import org.apache.velocity.app.VelocityEngine
import org.apache.velocity.app.event.EventCartridge
import org.apache.velocity.app.event.ReferenceInsertionEventHandler
import org.apache.velocity.context.Context
import org.jsoup.nodes.Entities
import org.apache.velocity.runtime.RuntimeConstants
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.StringWriter


@Singleton
class TemplateRenderer {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(TemplateRenderer::class.java)
        private val LINE_BREAKS = Regex("[\\r\\n]+")
    }

    private val ve = VelocityEngine()

    // user provided values (titles, names, ...) must never be interpreted as html inside a mail
    private val htmlEscaping = EventCartridge().apply {
        addReferenceInsertionEventHandler(ReferenceInsertionEventHandler { _: Context, _: String, value: Any? ->
            when (value) {
                null -> null
                is SafeHtml -> value.value
                else -> Entities.escape(value.toString())
            }
        })
    }

    init {
        // Block reflection-based introspection (e.g. $obj.class.forName(...)) to contain
        // server-side template injection if a malicious template is ever authored.
        ve.setProperty(
            RuntimeConstants.UBERSPECT_CLASSNAME,
            "org.apache.velocity.util.introspection.SecureUberspector"
        )
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

        val subjectWriter = StringWriter()
        ve.evaluate(VelocityContext(properties), subjectWriter, event.key, template.subject)
        val subject = subjectWriter.toString().replace(LINE_BREAKS, " ").trim()

        val context = VelocityContext(properties)
        if (!template.plain) htmlEscaping.attachToContext(context)
        val contentWriter = StringWriter()
        ve.evaluate(context, contentWriter, event.key, template.content)
        val plainContent = if (template.plain) contentWriter.toString() else null
        val htmlContent = if (!template.plain) contentWriter.toString() else null
        return Mail(subject = subject, htmlContent, plainContent)
    }

}
