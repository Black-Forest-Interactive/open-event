package de.sambalmueslie.openevent.core.model

data class ParticipateResponse(
    val registration: Registration,
    val participant: Participant?,
    val participants: List<Participant>,
    val status: ParticipateStatus
)
