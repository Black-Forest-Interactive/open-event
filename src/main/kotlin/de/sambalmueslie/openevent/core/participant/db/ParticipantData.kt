package de.sambalmueslie.openevent.core.participant.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountDetails
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantChangeRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipantDetails
import de.sambalmueslie.openevent.core.participant.api.ParticipantStatus
import de.sambalmueslie.openevent.core.registration.api.Registration
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Participant")
@Table(name = "participant")
data class ParticipantData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,

    @Column var registrationId: Long,
    @Column var accountId: Long,

    @Column var size: Long,
    @Column @Enumerated(EnumType.STRING) var status: ParticipantStatus,
    @Column var rank: Int,
    @Column var waitingList: Boolean,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : DataObject {
    companion object {
        fun create(
            registration: Registration,
            account: Account,
            request: ParticipantChangeRequest,
            timestamp: LocalDateTime
        ): ParticipantData {
            return ParticipantData(
                0,
                registration.id,
                account.id,
                request.size,
                request.status,
                request.rank,
                request.waitingList,
                timestamp
            )
        }
    }

    fun convert(account: AccountInfo): Participant {
        return Participant(id, size, status, rank, waitingList, account, updated ?: created)
    }

    fun convert(account: AccountDetails): ParticipantDetails {
        return ParticipantDetails(id, size, status, rank, waitingList, account, updated ?: created)
    }

    fun update(request: ParticipantChangeRequest, timestamp: LocalDateTime): ParticipantData {
        size = request.size
        status = request.status
        rank = request.rank
        waitingList = request.waitingList
        updated = timestamp
        return this
    }
}
