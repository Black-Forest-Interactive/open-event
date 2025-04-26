package de.sambalmueslie.openevent.core.participant.api

import de.sambalmueslie.openevent.core.registration.api.Registration

data class ParticipateResponse(
    val registration: Registration,
    val participant: Participant?,
    val participants: List<Participant>,
    val status: ParticipateStatus,
    val created: Boolean
)
