package de.sambalmueslie.openevent.infrastructure.settings.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class SettingChangeRequest(
    val key: String,
    val value: Any,
    val type: ValueType
) : BusinessObjectChangeRequest
