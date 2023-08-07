package de.sambalmueslie.openevent.core.logic.participant


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.ParticipantStorage
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ParticipantCrudService(
    private val storage: ParticipantStorage
) : BaseCrudService<Long, Participant, ParticipantChangeRequest>(storage, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParticipantCrudService::class.java)
    }


    fun get(registration: Registration): List<Participant> {
        return storage.get(registration)
    }

    fun add(registration: Registration, account: Account, request: ParticipateRequest): ParticipateResponse {
        val participants = storage.get(registration)
        val existing = participants.find { it.author.id == account.id }

        val availableSpace = registration.maxGuestAmount
        val usedSpace = participants.filter { it.status == ParticipantStatus.ACCEPTED }
            .filter { it.author.id != account.id }
            .sumOf { it.size }
        val remainingSpace = availableSpace - usedSpace
        val waitingList = remainingSpace <= 0 || participants.size > remainingSpace

        val changeRequest = ParticipantChangeRequest(
            request.size,
            ParticipantStatus.ACCEPTED,
            participants.size,
            waitingList
        )

        if (existing == null) {
            val participant = storage.create(changeRequest, account, registration)
            notifyCreated(participant)
        } else {
            val participant = storage.update(existing.id, changeRequest)
            notifyUpdated(participant)
        }

        val status: ParticipateStatus = if (waitingList) ParticipateStatus.WAITING_LIST else ParticipateStatus.ACCEPTED
        return ParticipateResponse(
            registration,
            storage.get(registration),
            status
        )
    }


}
