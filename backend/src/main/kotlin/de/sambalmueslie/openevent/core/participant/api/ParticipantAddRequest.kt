package de.sambalmueslie.openevent.core.participant.api

data class ParticipantAddRequest(
    val firstName: String,
    val lastName: String,
    val email: String,
    val phone: String,
    val mobile: String,
    val size: Long
)
