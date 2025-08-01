package de.sambalmueslie.openevent.core.participant


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.participant.api.*
import de.sambalmueslie.openevent.core.participant.db.ParticipantStorage
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.error.InvalidRequestException
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

    fun getDetails(registration: Registration): List<ParticipantDetails> {
        return storage.getDetails(registration)
    }

    override fun isValid(request: ParticipantChangeRequest) {
        if (request.size <= 0) throw InvalidRequestException("Size cannot be below zero")
    }

    fun change(
        actor: Account,
        registration: Registration,
        account: Account,
        request: ParticipateRequest,
        status: ParticipantStatus = ParticipantStatus.ACCEPTED
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
            status,
            participants.size,
            waitingList
        )

        val result = storage.create(changeRequest, account, registration)
        notifyCreated(actor, result)

        val status: ParticipateStatus = if (waitingList) ParticipateStatus.WAITING_LIST else ParticipateStatus.ACCEPTED
        return getResponse(registration, status, true, result)
    }

    fun remove(actor: Account, registration: Registration, account: Account): ParticipateResponse {
        val participants = storage.get(registration)
        val status = ParticipateStatus.DECLINED
        val existing = participants.find { it.author.id == account.id }
            ?: return getResponse(registration, status, false)

        storage.delete(existing.id)
        notifyDeleted(actor, existing)

        val result = updateWaitList(actor, registration)
        return ParticipateResponse(registration, existing, result, status, false)
    }

    fun change(
        actor: Account,
        registration: Registration,
        participantId: Long,
        request: ParticipateRequest
    ): ParticipateResponse {
        val status = ParticipateStatus.DECLINED
        val participant = storage.get(participantId) ?: return getResponse(registration, status, false)
        return change(actor, registration, participant, request)
    }

    private fun change(
        actor: Account,
        registration: Registration,
        participant: Participant,
        request: ParticipateRequest
    ): ParticipateResponse {

        val sizeChanged = participant.size != request.size
        if (!sizeChanged) {
            val result = updateWaitList(actor, registration)
            val status = if (participant.waitingList) ParticipateStatus.WAITING_LIST else ParticipateStatus.ACCEPTED
            return ParticipateResponse(registration, participant, result, status, false)
        }

        var waitingList = true
        var rank = Int.MAX_VALUE

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

        val status = if (waitingList) ParticipateStatus.WAITING_LIST else ParticipateStatus.ACCEPTED

        val changeRequest = ParticipantChangeRequest(
            request.size,
            ParticipantStatus.ACCEPTED,
            rank,
            waitingList
        )
        val p = storage.update(participant.id, changeRequest)
        val result = updateWaitList(actor, registration)
        return ParticipateResponse(registration, p, result, status, false)
    }

    fun remove(actor: Account, registration: Registration, participantId: Long): ParticipateResponse {
        val status = ParticipateStatus.DECLINED
        val participant = storage.get(participantId) ?: return getResponse(registration, status, false)
        storage.delete(participant.id)
        notifyDeleted(actor, participant)

        val result = updateWaitList(actor, registration)
        return ParticipateResponse(registration, participant, result, status, false)
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
        created: Boolean,
        participant: Participant? = null,
    ): ParticipateResponse {
        return ParticipateResponse(
            registration,
            participant,
            storage.get(registration),
            status,
            created
        )
    }

    fun deleteByRegistration(actor: Account, registration: Registration) {
        storage.get(registration).forEach {
            storage.delete(it.id)
            notifyDeleted(actor, it)
        }
    }


}
