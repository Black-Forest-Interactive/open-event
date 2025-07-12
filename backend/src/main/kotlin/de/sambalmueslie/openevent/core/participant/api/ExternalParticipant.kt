package de.sambalmueslie.openevent.core.participant.api

import de.sambalmueslie.openevent.gateway.external.account.PublicAccount
import de.sambalmueslie.openevent.gateway.external.account.toPublicAccount
import java.time.LocalDateTime

data class ExternalParticipant(
    val size: Long,
    val status: ParticipantStatus,
    val rank: Int,
    val waitingList: Boolean,
    val author: PublicAccount,
    val timestamp: LocalDateTime,
)


fun Participant.toExternalParticipant(): ExternalParticipant {
    return ExternalParticipant(size, status, rank, waitingList, author.toPublicAccount(), timestamp)
}
