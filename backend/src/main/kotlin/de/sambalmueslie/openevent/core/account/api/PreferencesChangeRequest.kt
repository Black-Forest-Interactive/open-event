package de.sambalmueslie.openevent.core.account.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class PreferencesChangeRequest(
    val emailNotificationsPreferences: EmailNotificationsPreferences,
    val communicationPreferences: CommunicationPreferences,
    val notificationPreferences: NotificationPreferences
) : BusinessObjectChangeRequest



