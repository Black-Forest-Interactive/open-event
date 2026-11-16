package de.sambalmueslie.openevent.core.account.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.account.api.*
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Preferences")
@Table(name = "preferences")
data class PreferencesData(
    @Id var id: Long,
    @Column(name = "email") @field:TypeDef(type = DataType.JSON) var emailNotificationsPreferences: EmailNotificationsPreferences,
    @Column(name = "communication") @field:TypeDef(type = DataType.JSON) var communicationPreferences: CommunicationPreferences,
    @Column(name = "notification") @field:TypeDef(type = DataType.JSON) var notificationPreferences: NotificationPreferences,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Preferences> {

    companion object {
        fun create(
            account: Account,
            request: PreferencesChangeRequest,
            timestamp: LocalDateTime
        ): PreferencesData {
            return PreferencesData(
                account.id,
                request.emailNotificationsPreferences,
                request.communicationPreferences,
                request.notificationPreferences,
                timestamp
            )
        }
    }

    override fun convert(): Preferences {
        return Preferences(
            id,
            emailNotificationsPreferences,
            communicationPreferences,
            notificationPreferences
        )
    }

    fun update(request: PreferencesChangeRequest, timestamp: LocalDateTime): PreferencesData {
        emailNotificationsPreferences = request.emailNotificationsPreferences
        communicationPreferences = request.communicationPreferences
        notificationPreferences = request.notificationPreferences
        updated = timestamp
        return this
    }

}
