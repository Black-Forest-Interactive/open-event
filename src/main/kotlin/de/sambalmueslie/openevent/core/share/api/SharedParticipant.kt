package de.sambalmueslie.openevent.core.share.api

import de.sambalmueslie.openevent.core.participant.api.ParticipantStatus
import java.time.LocalDateTime

data class SharedParticipant(
    val size: Long,
    val status: ParticipantStatus,
    val rank: Int,
    val waitingList: Boolean,
    val timestamp: LocalDateTime,
)
