package de.sambalmueslie.openevent.core.activity.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class ActivityTypeChangeRequest(
    val key: String,
) : BusinessObjectChangeRequest
