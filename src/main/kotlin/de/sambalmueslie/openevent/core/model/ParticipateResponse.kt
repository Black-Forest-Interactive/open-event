package de.sambalmueslie.openevent.core.model

data class ParticipateResponse(
    val registration: Registration,
    val participants: List<Participant>,
    val status: ParticipateStatus
)
