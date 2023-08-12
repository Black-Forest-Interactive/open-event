package de.sambalmueslie.openevent.core.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class TextResponse(
    val text: String
)
