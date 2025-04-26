package de.sambalmueslie.openevent.core.activity.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class ActivityChangeRequest(
    val title: String,
    val referenceId: Long
) : BusinessObjectChangeRequest
