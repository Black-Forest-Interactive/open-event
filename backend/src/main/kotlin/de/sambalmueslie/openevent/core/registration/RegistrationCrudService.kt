package de.sambalmueslie.openevent.core.registration


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountChangeRequest
import de.sambalmueslie.openevent.core.account.api.AccountSetupRequest
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.participant.ParticipantCrudService
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest
import de.sambalmueslie.openevent.core.registration.api.RegistrationDetails
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.core.registration.db.RegistrationStorage
import de.sambalmueslie.openevent.core.search.common.ChangeType
import de.sambalmueslie.openevent.core.search.common.SearchUpdateEvent
import de.sambalmueslie.openevent.error.InvalidRequestException
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class RegistrationCrudService(
    private val storage: RegistrationStorage,
    private val participantCrudService: ParticipantCrudService,
    private val accountCrudService: AccountCrudService,
) : BaseCrudService<Long, Registration, RegistrationChangeRequest, RegistrationChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RegistrationCrudService::class.java)
    }

    private var searchListener: RegistrationSearchListener? = null

    fun registerSearch(listener: RegistrationSearchListener) {
        this.searchListener = listener
    }

    fun create(actor: Account, event: Event, request: RegistrationChangeRequest): Registration {
        val result = storage.create(request, event)
        notifyCreated(actor, result)
        return result
    }


    override fun isValid(request: RegistrationChangeRequest) {
        if (request.maxGuestAmount <= 0) throw InvalidRequestException("Max guest must be positive number")
    }

    fun findByEvent(event: Event): Registration? {
        return storage.findByEvent(event)
    }


    fun updateByEvent(actor: Account, event: Event, request: RegistrationChangeRequest): Registration {
        val existing = storage.findByEvent(event)
        return if (existing != null) {
            val result = storage.update(existing.id, request)
            notifyUpdated(actor, result)
            result
        } else {
            val result = storage.create(request, event)
            notifyCreated(actor, result)
            result
        }
    }

    fun deleteByEvent(actor: Account, event: Event): Registration? {
        val existing = storage.findByEvent(event) ?: return null
        participantCrudService.deleteByRegistration(actor, existing)
        storage.delete(existing.id)
        notifyDeleted(actor, existing)
        return existing
    }

    fun findByEventIds(eventIds: Set<Long>): List<Registration> {
        return storage.findByEventIds(eventIds)
    }

    fun getParticipants(id: Long): List<Participant> {
        val registration = get(id) ?: return emptyList()
        return participantCrudService.get(registration)
    }

    fun addParticipant(actor: Account, id: Long, account: Account, request: ParticipateRequest): ParticipateResponse? {
        val registration = get(id) ?: return null
        return changeParticipant(actor, registration, account, request)
    }

    fun changeParticipant(
        actor: Account,
        id: Long,
        account: Account,
        request: ParticipateRequest
    ): ParticipateResponse? {
        val registration = get(id) ?: return null
        return changeParticipant(actor, registration, account, request)
    }

    fun removeParticipant(actor: Account, id: Long, account: Account): ParticipateResponse? {
        val registration = get(id) ?: return null
        val result = participantCrudService.remove(actor, registration, account)
        result.participant?.let { p ->
            notify { it.participantRemoved(actor, registration, p) }
        }
        updateSearch(registration, ChangeType.UPDATED)
        return result
    }


    fun addParticipant(actor: Account, id: Long, request: ParticipantAddRequest): ParticipateResponse? {
        val registration = get(id) ?: return null

        val info = accountCrudService.setup(
            actor, AccountSetupRequest(
                AccountChangeRequest(
                    "${request.firstName} ${request.lastName}",
                    "",
                    null
                ),
                ProfileChangeRequest(
                    request.email,
                    request.phone,
                    request.mobile,
                    request.firstName,
                    request.lastName,
                    null, null, null, null, ""
                )
            )
        )
        val account = accountCrudService.get(info.id) ?: return null
        return changeParticipant(actor, registration, account, ParticipateRequest(request.size))
    }

    private fun changeParticipant(
        actor: Account,
        registration: Registration,
        account: Account,
        request: ParticipateRequest
    ): ParticipateResponse {
        val result = participantCrudService.change(actor, registration, account, request)
        return processResponse(actor, registration, result)
    }

    fun changeParticipant(
        actor: Account,
        id: Long,
        participantId: Long,
        request: ParticipateRequest
    ): ParticipateResponse? {
        val registration = get(id) ?: return null
        val result = participantCrudService.change(actor, registration, participantId, request)
        return processResponse(actor, registration, result)
    }

    private fun processResponse(
        actor: Account,
        registration: Registration,
        result: ParticipateResponse
    ): ParticipateResponse {
        result.participant?.let { p ->
            if (result.created) {
                notify { it.participantCreated(actor, registration, p, result.status) }
            } else {
                notify { it.participantChanged(actor, registration, p, result.status) }
            }
        }
        updateSearch(registration, ChangeType.UPDATED)
        return result
    }


    fun removeParticipant(actor: Account, id: Long, participantId: Long): ParticipateResponse? {
        val registration = get(id) ?: return null
        val result = participantCrudService.remove(actor, registration, participantId)
        result.participant?.let { p ->
            notify { it.participantRemoved(actor, registration, p) }
        }
        updateSearch(registration, ChangeType.UPDATED)
        return result
    }

    fun getInfo(id: Long): RegistrationInfo? {
        val registration = get(id) ?: return null
        val participants = participantCrudService.get(registration)
        return RegistrationInfo(registration, participants)
    }

    fun getDetails(id: Long): RegistrationDetails? {
        val registration = get(id) ?: return null
        val participants = participantCrudService.getDetails(registration)
        return RegistrationDetails(registration, participants)
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

    private fun updateSearch(registration: Registration, type: ChangeType) {
        if (this.searchListener == null) return
        this.searchListener?.updateSearch(SearchUpdateEvent(registration, type))
    }


}
