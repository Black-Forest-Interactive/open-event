package de.sambalmueslie.openevent.core.participant.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
data class Participant(
    override val id: Long,
    val size: Long,
    val status: ParticipantStatus,
    val rank: Int,
    val waitingList: Boolean,
    val author: AccountInfo,
    val timestamp: LocalDateTime,
) : BusinessObject<Long>
