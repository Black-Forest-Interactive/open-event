package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class NotificationSettingChangeRequest(
    val name: String,
    val enabled: Boolean
) : BusinessObjectChangeRequest

