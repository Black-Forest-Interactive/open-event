package de.sambalmueslie.openevent.core.participant.api


import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
class ParticipantChangeRequest(
    val size: Long,
    val status: ParticipantStatus,
    val rank: Int,
    val waitingList: Boolean,
) : BusinessObjectChangeRequest
