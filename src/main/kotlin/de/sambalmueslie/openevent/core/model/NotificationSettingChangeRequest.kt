package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NotificationSettingChangeRequest(
    val name: String,
    val enabled: Boolean
) : BusinessObjectChangeRequest

