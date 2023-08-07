package de.sambalmueslie.openevent.storage.notification

import de.sambalmueslie.openevent.core.model.NotificationScheme
import de.sambalmueslie.openevent.core.model.NotificationTemplate
import de.sambalmueslie.openevent.core.model.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "NotificationTemplate")
@Table(name = "notification_template")
data class NotificationTemplateData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column val schemeId: Long,
    @Column var subject: String,
    @Column var lang: String,
    @Column var content: String,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<NotificationTemplate> {

    companion object {
        fun create(
            scheme: NotificationScheme,
            request: NotificationTemplateChangeRequest,
            timestamp: LocalDateTime
        ): NotificationTemplateData {
            return NotificationTemplateData(
                0,
                scheme.id,
                request.subject,
                request.lang,
                request.content,
                timestamp
            )
        }
    }

    override fun convert(): NotificationTemplate {
        return NotificationTemplate(id, subject, lang, content)
    }

    fun update(request: NotificationTemplateChangeRequest, timestamp: LocalDateTime): NotificationTemplateData {
        subject = request.subject
        lang = request.lang
        content = request.content
        updated = timestamp
        return this
    }

}

