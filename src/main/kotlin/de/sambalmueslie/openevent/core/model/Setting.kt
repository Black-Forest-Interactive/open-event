package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject

data class Setting(
    override val id: Long,
    val key: String,
    val value: Any,
    val type: ValueType
) : BusinessObject<Long>
