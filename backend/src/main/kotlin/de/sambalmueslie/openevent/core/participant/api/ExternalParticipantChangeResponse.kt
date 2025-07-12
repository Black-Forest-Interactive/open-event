package de.sambalmueslie.openevent.core.participant.api

data class ExternalParticipantChangeResponse(
    val status: ParticipateStatus
) {
    companion object {
        fun failed() = ExternalParticipantChangeResponse(ParticipateStatus.FAILED)
    }
}

