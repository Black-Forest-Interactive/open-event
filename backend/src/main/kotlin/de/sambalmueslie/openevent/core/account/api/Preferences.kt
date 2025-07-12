package de.sambalmueslie.openevent.core.account.api

import de.sambalmueslie.openevent.common.BusinessObject

data class Preferences(
    override val id: Long,
    val emailNotificationsPreferences: EmailNotificationsPreferences,
    val communicationPreferences: CommunicationPreferences,
    val notificationPreferences: NotificationPreferences
) : BusinessObject<Long>

data class EmailNotificationsPreferences(
    val enabled: Boolean = true
    // TODO add some content
)

data class CommunicationPreferences(
    val enabled: Boolean = true
    // TODO add some content
)

data class NotificationPreferences(
    val enabled: Boolean = true
    // TODO add some content
)