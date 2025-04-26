package de.sambalmueslie.openevent.core.activity.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class ActivitySourceChangeRequest(
    val key: String,
) : BusinessObjectChangeRequest
