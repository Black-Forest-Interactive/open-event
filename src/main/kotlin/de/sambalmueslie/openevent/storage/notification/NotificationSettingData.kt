package de.sambalmueslie.openevent.storage.notification

import de.sambalmueslie.openevent.core.model.NotificationSetting
import de.sambalmueslie.openevent.core.model.NotificationSettingChangeRequest
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime


@Entity(name = "NotificationSettings")
@Table(name = "notification_settings")
data class NotificationSettingData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var name: String,
    @Column var enabled: Boolean,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<NotificationSetting> {

    companion object {
        fun create(
            request: NotificationSettingChangeRequest,
            timestamp: LocalDateTime
        ): NotificationSettingData {
            return NotificationSettingData(
                0,
                request.name,
                request.enabled,
                timestamp
            )
        }
    }

    override fun convert(): NotificationSetting {
        return NotificationSetting(id, name, enabled)
    }

    fun update(request: NotificationSettingChangeRequest, timestamp: LocalDateTime): NotificationSettingData {
        name = request.name
        enabled = request.enabled
        updated = timestamp
        return this
    }

    fun setEnabled(value: Boolean, timestamp: LocalDateTime): NotificationSettingData {
        this.enabled = value
        updated = timestamp
        return this
    }
}
