package de.sambalmueslie.openevent.core.share.api

import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus

data class SharedParticipateResponse(
    val registration: SharedRegistration,
    val participant: Participant?,
    val status: ParticipateStatus,
    val created: Boolean
)