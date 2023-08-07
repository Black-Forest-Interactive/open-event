package de.sambalmueslie.openevent.core.logic.registration


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.logic.participant.ParticipantCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.RegistrationStorage
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class RegistrationCrudService(
    private val storage: RegistrationStorage,
    private val participantCrudService: ParticipantCrudService
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
        return participantCrudService.add(registration, account, request)
    }

}
