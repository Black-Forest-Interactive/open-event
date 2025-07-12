package de.sambalmueslie.openevent.core.account.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class PreferencesChangeRequest(
    val emailNotificationsPreferences: EmailNotificationsPreferences,
    val communicationPreferences: CommunicationPreferences,
    val notificationPreferences: NotificationPreferences
) : BusinessObjectChangeRequest



