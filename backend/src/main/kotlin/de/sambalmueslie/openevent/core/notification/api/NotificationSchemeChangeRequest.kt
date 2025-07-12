package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class NotificationSchemeChangeRequest(
    val name: String,
    val enabled: Boolean,
) : BusinessObjectChangeRequest
