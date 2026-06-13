package de.sambalmueslie.openevent.core.participant.api

data class ExternalParticipantChangeResponse(
    val participant: ExternalParticipant?,
    val status: ParticipateStatus
) {
    companion object {
        fun failed() = ExternalParticipantChangeResponse(null, ParticipateStatus.FAILED)
    }
}

