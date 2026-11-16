package de.sambalmueslie.openevent.core.finder.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest

data class FinderChangeRequest(
    val name: String,
    val request: EventSearchRequest,
) : BusinessObjectChangeRequest
