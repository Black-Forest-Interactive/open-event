package de.sambalmueslie.openevent.core.logic.registration


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.participant.ParticipantCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.RegistrationStorage
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class RegistrationCrudService(
    private val storage: RegistrationStorage,
    private val participantCrudService: ParticipantCrudService,
    private val accountCrudService: AccountCrudService
) : BaseCrudService<Long, Registration, RegistrationChangeRequest>(storage, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RegistrationCrudService::class.java)
    }

    fun create(event: Event, request: RegistrationChangeRequest): Registration {
        val result = storage.create(request, event)
        notifyCreated(result)
        return result
    }


    fun findByEvent(event: Event): Registration? {
        return storage.findByEvent(event)
    }


    fun updateByEvent(event: Event, request: RegistrationChangeRequest): Registration {
        val existing = storage.findByEvent(event)
        return if (existing != null) {
            val result = storage.update(existing.id, request)
            notifyUpdated(result)
            result
        } else {
            val result = storage.create(request, event)
            notifyCreated(result)
            result
        }
    }

    fun deleteByEvent(event: Event): Registration? {
        val existing = storage.findByEvent(event) ?: return null
        storage.delete(existing.id)
        notifyDeleted(existing)
        return existing
    }

    fun findByEventIds(eventIds: Set<Long>): List<Registration> {
        return storage.findByEventIds(eventIds)
    }

    fun getParticipants(id: Long): List<Participant> {
        val registration = get(id) ?: return emptyList()
        return participantCrudService.get(registration)
    }

    fun addParticipant(id: Long, account: Account, request: ParticipateRequest): ParticipateResponse? {
        val registration = get(id) ?: return null
        return participantCrudService.change(registration, account, request)
    }

    fun changeParticipant(id: Long, account: Account, request: ParticipateRequest): ParticipateResponse? {
        val registration = get(id) ?: return null
        return participantCrudService.change(registration, account, request)
    }

    fun removeParticipant(id: Long, account: Account): ParticipateResponse? {
        val registration = get(id) ?: return null
        return participantCrudService.remove(registration, account)
    }


    fun addParticipant(id: Long, request: ParticipantAddRequest): ParticipateResponse? {
        val registration = get(id) ?: return null
        val account = accountCrudService.create(
            AccountChangeRequest(
                "${request.firstName} ${request.lastName}",
                request.firstName,
                request.lastName,
                request.email,
                "",
                null
            )
        )
        return participantCrudService.change(registration, account, ParticipateRequest(request.size))
    }

    fun changeParticipant(id: Long, participantId: Long, request: ParticipateRequest): ParticipateResponse? {
        val registration = get(id) ?: return null
        return participantCrudService.change(registration, participantId, request)
    }

    fun removeParticipant(id: Long, participantId: Long): ParticipateResponse? {
        val registration = get(id) ?: return null
        return participantCrudService.remove(registration, participantId)
    }

    fun getInfo(id: Long): RegistrationInfo? {
        val registration = get(id) ?: return null
        val participants = participantCrudService.get(registration)
        return RegistrationInfo(registration, participants)
    }

    fun findInfoByEvent(event: Event): RegistrationInfo? {
        val registration = storage.findByEvent(event) ?: return null
        val participants = participantCrudService.get(registration)
        return RegistrationInfo(registration, participants)
    }

    fun findInfosByEventIds(eventIds: Set<Long>): List<RegistrationInfo> {
        val registrations = storage.findByEventIds(eventIds)
        val participants = participantCrudService.get(registrations)
        return participants.map { RegistrationInfo(it.key, it.value) }
    }
}
