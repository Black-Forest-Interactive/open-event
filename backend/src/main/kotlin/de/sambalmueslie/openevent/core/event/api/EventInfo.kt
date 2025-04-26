package de.sambalmueslie.openevent.core.event.api

import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class EventInfo(
    val event: Event,
    val location: Location?,
    val registration: RegistrationInfo?,
    val categories: List<Category>,
    val canEdit: Boolean
)