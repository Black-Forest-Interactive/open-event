package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NotificationType(
    override val id: Long,
    val key: String,
    val name: String,
    val description: String
) : BusinessObject<Long>
