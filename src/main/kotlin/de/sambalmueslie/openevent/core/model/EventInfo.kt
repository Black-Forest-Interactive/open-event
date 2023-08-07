package de.sambalmueslie.openevent.core.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class EventInfo(
    val event: Event,
    val location: Location?,
    val registration: Registration?,
    val categories: List<Category>
)
