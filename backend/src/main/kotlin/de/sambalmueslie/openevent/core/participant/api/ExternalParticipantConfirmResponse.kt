package de.sambalmueslie.openevent.core.participant.api

data class ExternalParticipantConfirmResponse(
    val participant: ExternalParticipant?,
    val status: ParticipateStatus
) {
    companion object {
        fun failed() = ExternalParticipantConfirmResponse(null, ParticipateStatus.FAILED)
    }
}
