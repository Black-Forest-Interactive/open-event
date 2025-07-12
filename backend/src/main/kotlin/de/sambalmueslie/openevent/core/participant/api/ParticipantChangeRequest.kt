package de.sambalmueslie.openevent.core.participant.api


import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

class ParticipantChangeRequest(
    val size: Long,
    val status: ParticipantStatus,
    val rank: Int,
    val waitingList: Boolean,
) : BusinessObjectChangeRequest
