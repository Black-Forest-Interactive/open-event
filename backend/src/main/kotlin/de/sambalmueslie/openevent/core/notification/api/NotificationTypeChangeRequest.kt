package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class NotificationTypeChangeRequest(
    val key: String,
    val name: String,
    val description: String
) : BusinessObjectChangeRequest
