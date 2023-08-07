package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NotificationScheme(
    override val id: Long,
    val name: String,
    val enabled: Boolean,
    val plain: Boolean,
) : BusinessObject<Long>
