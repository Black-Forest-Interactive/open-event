package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NotificationSchemeChangeRequest(
    val name: String,
    val enabled: Boolean,
    val plain: Boolean,
) : BusinessObjectChangeRequest
