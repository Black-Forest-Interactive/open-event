package de.sambalmueslie.openevent.core.activity.api

import de.sambalmueslie.openevent.common.BusinessObject

data class ActivityType(
    override val id: Long,
    val key: String,
): BusinessObject<Long>
