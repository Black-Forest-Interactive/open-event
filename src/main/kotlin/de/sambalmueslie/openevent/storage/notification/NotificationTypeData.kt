package de.sambalmueslie.openevent.storage.notification

import de.sambalmueslie.openevent.core.model.NotificationType
import de.sambalmueslie.openevent.core.model.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "NotificationType")
@Table(name = "notification_type")
data class NotificationTypeData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var key: String,
    @Column var name: String,
    @Column var description: String,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<NotificationType> {

    companion object {
        fun create(
            request: NotificationTypeChangeRequest,
            timestamp: LocalDateTime
        ): NotificationTypeData {
            return NotificationTypeData(
                0,
                request.key,
                request.name,
                request.description,
                timestamp
            )
        }
    }

    override fun convert(): NotificationType {
        return NotificationType(id, key, name, description)
    }

    fun update(
        request: NotificationTypeChangeRequest,
        timestamp: LocalDateTime
    ): NotificationTypeData {
        key = request.key
        name = request.name
        description = request.description
        updated = timestamp
        return this
    }

}
