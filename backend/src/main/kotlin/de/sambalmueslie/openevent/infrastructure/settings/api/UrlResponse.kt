package de.sambalmueslie.openevent.infrastructure.settings.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class UrlResponse(
    val url: String
)
