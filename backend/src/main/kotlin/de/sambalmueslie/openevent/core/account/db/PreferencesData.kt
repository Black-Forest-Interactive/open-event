package de.sambalmueslie.openevent.core.account.db

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.account.api.*
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Preferences")
@Table(name = "preferences")
data class PreferencesData(
    @Id var id: Long,
    @Column(name = "email") var emailNotificationsPreferencesJson: String,
    @Column(name = "communication") var communicationPreferencesJson: String,
    @Column(name = "notification") var notificationPreferencesJson: String,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Preferences> {

    companion object {
        private val mapper = ObjectMapper().registerKotlinModule()
        fun create(
            account: Account,
            request: PreferencesChangeRequest,
            timestamp: LocalDateTime
        ): PreferencesData {
            val e = mapper.writeValueAsString(request.emailNotificationsPreferences)
            val c = mapper.writeValueAsString(request.communicationPreferences)
            val n = mapper.writeValueAsString(request.notificationPreferences)

            val data = PreferencesData(account.id, e, c, n, timestamp)
            data.emailNotificationsPreferences = request.emailNotificationsPreferences
            data.communicationPreferences = request.communicationPreferences
            data.notificationPreferences = request.notificationPreferences
            return data
        }
    }

    override fun convert(): Preferences {
        return Preferences(
            id,
            getEmailNotificationsPreferencesObj(),
            getCommunicationPreferencesObj(),
            getNotificationsPreferencesObj()
        )
    }

    fun update(request: PreferencesChangeRequest, timestamp: LocalDateTime): PreferencesData {
        emailNotificationsPreferencesJson = mapper.writeValueAsString(request.emailNotificationsPreferences)
        communicationPreferencesJson = mapper.writeValueAsString(request.communicationPreferences)
        notificationPreferencesJson = mapper.writeValueAsString(request.notificationPreferences)

        emailNotificationsPreferences = request.emailNotificationsPreferences
        communicationPreferences = request.communicationPreferences
        notificationPreferences = request.notificationPreferences
        updated = timestamp
        return this
    }


    @Transient
    private var emailNotificationsPreferences: EmailNotificationsPreferences? = null

    @Transient
    fun getEmailNotificationsPreferencesObj(): EmailNotificationsPreferences {
        if (emailNotificationsPreferences == null) {
            emailNotificationsPreferences =
                mapper.readValue<EmailNotificationsPreferences>(emailNotificationsPreferencesJson)
        }
        return emailNotificationsPreferences!!
    }

    @Transient
    private var communicationPreferences: CommunicationPreferences? = null

    @Transient
    fun getCommunicationPreferencesObj(): CommunicationPreferences {
        if (communicationPreferences == null) {
            communicationPreferences = mapper.readValue<CommunicationPreferences>(communicationPreferencesJson)
        }
        return communicationPreferences!!
    }

    @Transient
    private var notificationPreferences: NotificationPreferences? = null

    @Transient
    fun getNotificationsPreferencesObj(): NotificationPreferences {
        if (notificationPreferences == null) {
            notificationPreferences = mapper.readValue<NotificationPreferences>(notificationPreferencesJson)
        }
        return notificationPreferences!!
    }

}
