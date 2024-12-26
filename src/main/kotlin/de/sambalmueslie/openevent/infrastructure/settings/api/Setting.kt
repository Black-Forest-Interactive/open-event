package de.sambalmueslie.openevent.infrastructure.settings.api

import de.sambalmueslie.openevent.common.BusinessObject

data class Setting(
    override val id: Long,
    val key: String,
    val value: Any,
    val type: ValueType
) : BusinessObject<Long>
