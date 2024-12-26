package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NotificationTypeChangeRequest(
    val key: String,
    val name: String,
    val description: String
) : BusinessObjectChangeRequest
