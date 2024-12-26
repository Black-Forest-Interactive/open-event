package de.sambalmueslie.openevent.core.category.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class CategoryChangeRequest(
    val name: String,
    val iconUrl: String
) : BusinessObjectChangeRequest
