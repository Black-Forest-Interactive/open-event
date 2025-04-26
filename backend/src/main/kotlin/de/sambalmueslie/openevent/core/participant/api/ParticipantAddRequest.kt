package de.sambalmueslie.openevent.core.participant.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ParticipantAddRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val mobile: String,
    val size: Long
)
