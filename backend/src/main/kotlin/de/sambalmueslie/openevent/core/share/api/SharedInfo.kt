package de.sambalmueslie.openevent.core.share.api

import de.sambalmueslie.openevent.core.category.api.Category
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class SharedInfo(
    val event: SharedEvent,
    val location: SharedLocation?,
    val registration: SharedRegistration?,
    val categories: List<Category>,
)
