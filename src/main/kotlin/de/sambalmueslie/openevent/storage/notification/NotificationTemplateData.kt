package de.sambalmueslie.openevent.storage.notification

import de.sambalmueslie.openevent.core.model.NotificationTemplate
import de.sambalmueslie.openevent.core.model.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.model.NotificationType
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "NotificationTemplate")
@Table(name = "notification_template")
data class NotificationTemplateData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column val typeId: Long,
    @Column var subject: String,
    @Column var lang: String,
    @Column var content: String,
    @Column var plain: Boolean,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<NotificationTemplate> {

    companion object {
        fun create(
            type: NotificationType,
            request: NotificationTemplateChangeRequest,
            timestamp: LocalDateTime
        ): NotificationTemplateData {
            return NotificationTemplateData(
                0,
                type.id,
                request.subject,
                request.lang,
                request.content,
                request.plain,
                timestamp
            )
        }
    }

    override fun convert(): NotificationTemplate {
        return NotificationTemplate(id, subject, lang, content, plain)
    }

    fun update(request: NotificationTemplateChangeRequest, timestamp: LocalDateTime): NotificationTemplateData {
        subject = request.subject
        lang = request.lang
        content = request.content
        plain = request.plain
        updated = timestamp
        return this
    }

}

