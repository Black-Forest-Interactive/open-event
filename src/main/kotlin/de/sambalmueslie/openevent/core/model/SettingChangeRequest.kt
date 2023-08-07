package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest

data class SettingChangeRequest(
    val key: String,
    val value: Any,
    val type: ValueType
) : BusinessObjectChangeRequest
