package de.sambalmueslie.openevent.storage.notification

import de.sambalmueslie.openevent.core.model.NotificationScheme
import de.sambalmueslie.openevent.core.model.NotificationSchemeChangeRequest
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "NotificationScheme")
@Table(name = "notification_scheme")
data class NotificationSchemeData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var name: String,
    @Column var enabled: Boolean,


    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<NotificationScheme> {

    companion object {
        fun create(
            request: NotificationSchemeChangeRequest,
            timestamp: LocalDateTime
        ): NotificationSchemeData {
            return NotificationSchemeData(
                0,
                request.name,
                request.enabled,
                timestamp
            )
        }
    }

    override fun convert(): NotificationScheme {
        return NotificationScheme(id, name, enabled)
    }

    fun update(request: NotificationSchemeChangeRequest, timestamp: LocalDateTime): NotificationSchemeData {
        name = request.name
        enabled = request.enabled
        updated = timestamp
        return this
    }

    fun setEnabled(value: Boolean, timestamp: LocalDateTime): NotificationSchemeData {
        this.enabled = value
        updated = timestamp
        return this
    }
}
