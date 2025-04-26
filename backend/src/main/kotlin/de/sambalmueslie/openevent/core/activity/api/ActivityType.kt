package de.sambalmueslie.openevent.core.activity.api

import de.sambalmueslie.openevent.common.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ActivityType(
    override val id: Long,
    val key: String,
): BusinessObject<Long>
