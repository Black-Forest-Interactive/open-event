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
) : BaseCrudService<Long, Participant, ParticipantChangeRequest, ParticipantChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ParticipantCrudService::class.java)
    }


    fun get(registration: Registration): List<Participant> {
        return storage.get(registration)
    }

    fun get(registration: List<Registration>): Map<Registration, List<Participant>> {
        return storage.get(registration)
    }

    fun change(
        actor: Account,
        registration: Registration,
        account: Account,
        request: ParticipateRequest
    ): ParticipateResponse {
        if (request.size <= 0) return remove(actor, registration, account)
        val participant = storage.findByAccount(registration, account)
        if (participant != null) {
            return change(actor, registration, participant, request)
        }

        val participants = storage.get(registration)

        val availableSpace = registration.maxGuestAmount
        val usedSpace = participants.filter { it.status == ParticipantStatus.ACCEPTED }
            .sumOf { it.size }
        val remainingSpace = availableSpace - usedSpace
        val waitingList = remainingSpace < 0 || request.size > remainingSpace

        val changeRequest = ParticipantChangeRequest(
            request.size,
            ParticipantStatus.ACCEPTED,
            participants.size,
            waitingList
        )

        val result = storage.create(changeRequest, account, registration)
        notifyCreated(actor, result)

        val status: ParticipateStatus = if (waitingList) ParticipateStatus.WAITING_LIST else ParticipateStatus.ACCEPTED
        return getResponse(registration, status, result)
    }

    fun remove(actor: Account, registration: Registration, account: Account): ParticipateResponse {
        val participants = storage.get(registration)
        val status = ParticipateStatus.DECLINED
        val existing = participants.find { it.author.id == account.id } ?: return getResponse(registration, status)

        storage.delete(existing.id)
        notifyDeleted(actor, existing)

        val result = updateWaitList(actor, registration)
        return ParticipateResponse(registration, existing, result, status)
    }

    fun change(
        actor: Account,
        registration: Registration,
        participantId: Long,
        request: ParticipateRequest
    ): ParticipateResponse {
        val status = ParticipateStatus.DECLINED
        val participant = storage.get(participantId) ?: return getResponse(registration, status)
        return change(actor, registration, participant, request)
    }

    private fun change(
        actor: Account,
        registration: Registration,
        participant: Participant,
        request: ParticipateRequest
    ): ParticipateResponse {
        val status = ParticipateStatus.DECLINED
        val sizeChanged = participant.size != request.size

        var waitingList = if (sizeChanged) participant.waitingList else true
        var rank = if (sizeChanged) Int.MAX_VALUE else participant.rank

        if (sizeChanged) {
            val participants = storage.get(registration)

            val availableSpace = registration.maxGuestAmount
            val usedSpace = participants.filter { it.status == ParticipantStatus.ACCEPTED }
                .filter { it.id != participant.id }
                .sumOf { it.size }
            val remainingSpace = availableSpace - usedSpace
            if (remainingSpace >= request.size) {
                waitingList = false
                rank = participant.rank
            }
        }

        val changeRequest = ParticipantChangeRequest(
            request.size,
            ParticipantStatus.ACCEPTED,
            rank,
            waitingList
        )
        val participant = storage.update(participant.id, changeRequest)
        val result = updateWaitList(actor, registration)
        return ParticipateResponse(registration, participant, result, status)
    }

    fun remove(actor: Account, registration: Registration, participantId: Long): ParticipateResponse {
        val status = ParticipateStatus.DECLINED
        val participant = storage.get(participantId) ?: return getResponse(registration, status)
        storage.delete(participant.id)
        notifyDeleted(actor, participant)

        val result = updateWaitList(actor, registration)
        return ParticipateResponse(registration, participant, result, status)
    }

    private fun updateWaitList(actor: Account, registration: Registration): List<Participant> {
        val availableSpace = registration.maxGuestAmount
        var remainingSpace = availableSpace.toLong()

        val acceptedParticipants = storage.get(registration)
            .filter { it.status == ParticipantStatus.ACCEPTED }
            .sortedBy { it.rank }

        val participantList = mutableListOf<Participant>()
        val waitList = mutableListOf<Participant>()

        acceptedParticipants.forEach { p ->
            val suitable = remainingSpace >= p.size
            if (suitable) {
                remainingSpace -= p.size
                participantList.add(p)
            } else {
                waitList.add(p)
            }
        }

        val participantResult =
            participantList.mapIndexed { index, participant -> updateParticipant(actor, participant, index, false) }
        val waitListResult = waitList.mapIndexed { index, participant ->
            updateParticipant(
                actor,
                participant,
                participantList.size + index,
                true
            )
        }

        return participantResult + waitListResult
    }

    private fun updateParticipant(actor: Account, participant: Participant, rank: Int, waitList: Boolean): Participant {
        val changed = participant.rank != rank || participant.waitingList != waitList
        if (!changed) return participant

        val request = ParticipantChangeRequest(
            participant.size,
            ParticipantStatus.ACCEPTED,
            rank,
            waitList
        )
        val result = storage.update(participant.id, request)
        notifyUpdated(actor, result)
        return result
    }


    private fun getResponse(
        registration: Registration,
        status: ParticipateStatus,
        participant: Participant? = null,
    ): ParticipateResponse {
        return ParticipateResponse(
            registration,
            participant,
            storage.get(registration),
            status
        )
    }

}
