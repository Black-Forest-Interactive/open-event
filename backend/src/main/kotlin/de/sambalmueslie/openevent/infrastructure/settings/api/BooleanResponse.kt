package de.sambalmueslie.openevent.infrastructure.settings.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class BooleanResponse(
    val text: Boolean
)
