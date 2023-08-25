package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NotificationTypeChangeRequest(
    val key: String,
    val name: String,
    val description: String
) : BusinessObjectChangeRequest
