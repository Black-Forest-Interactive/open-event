package de.sambalmueslie.openevent.core.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ParticipantAddRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val size: Long
)
