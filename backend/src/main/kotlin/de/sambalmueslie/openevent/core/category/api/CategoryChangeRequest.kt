package de.sambalmueslie.openevent.core.category.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class CategoryChangeRequest(
    val name: String,
    val iconUrl: String
) : BusinessObjectChangeRequest
