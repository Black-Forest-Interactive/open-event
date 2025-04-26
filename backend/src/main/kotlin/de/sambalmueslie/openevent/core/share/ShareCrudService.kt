package de.sambalmueslie.openevent.core.share

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.EventChangeListener
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import de.sambalmueslie.openevent.core.share.api.*
import de.sambalmueslie.openevent.core.share.db.ShareStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ShareCrudService(
    private val storage: ShareStorage,
    private val accountService: AccountCrudService,
    private val eventService: EventCrudService,
    private val registrationService: RegistrationCrudService,
) : BaseCrudService<String, Share, ShareChangeRequest, ShareChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ShareCrudService::class.java)
    }

    init {
        eventService.register(object : EventChangeListener {
            override fun publishedChanged(actor: Account, event: Event) {
                if (event.published) return
                val share = storage.findByEvent(event) ?: return
                setPublished(actor, share.id, PatchRequest(false))
            }

            override fun handleDeleted(actor: Account, obj: Event) {
                val share = storage.findByEvent(obj) ?: return
                delete(actor, share.id)
            }
        })
    }

    fun create(actor: Account, request: ShareChangeRequest): Share {
        val event = eventService.get(request.eventId) ?: throw InvalidRequestException("Cannot find event for share")
        return create(actor, request, event)
    }

    fun create(actor: Account, request: ShareChangeRequest, event: Event): Share {
        val result = storage.create(request, event, actor)
        notifyCreated(actor, result)
        return result
    }

    @Deprecated("thats handled by the event controller on public")
    fun getInfo(id: String, account: Account?): SharedInfo? {
        val share = storage.get(id) ?: return null
        val event = eventService.getInfo(share.eventId, account) ?: return null
        return share.toInfo(event)
    }

    fun getAllForAccount(account: Account, pageable: Pageable): Page<Share> {
        return storage.getAllForAccount(account, pageable)
    }

    fun setPublished(actor: Account, id: String, value: PatchRequest<Boolean>): Share? {
        val result = storage.setPublished(id, value) ?: return null
        notify { it.publishedChanged(actor, result) }
        return result
    }

    private fun Share.toInfo(event: EventInfo): SharedInfo {
        return SharedInfo(
            event.event.toSharedEvent(),
            event.location?.toSharedLocation(),
            event.registration?.toSharedRegistration(),
            event.categories
        )
    }


    private fun Event.toSharedEvent(): SharedEvent {
        return SharedEvent(
            id, owner.toSharedAccount(), start, finish, title, shortText, longText, imageUrl, iconUrl, published
        )
    }

    private fun AccountInfo.toSharedAccount(): SharedAccount {
        return SharedAccount(id, name)
    }

    private fun Location.toSharedLocation(): SharedLocation {
        return SharedLocation(street, streetNumber, zip, city, country, additionalInfo, lat, lon, size)
    }

    private fun RegistrationInfo.toSharedRegistration(): SharedRegistration {
        return SharedRegistration(
            registration.maxGuestAmount, registration.interestedAllowed, registration.ticketsEnabled,
            participants.map { it.toSharedParticipant() }
        )
    }

    private fun Participant.toSharedParticipant(): SharedParticipant {
        return SharedParticipant(size, status, rank, waitingList, timestamp)
    }

    fun findByEvent(eventId: Long): Share? {
        val event = eventService.get(eventId) ?: return null
        return storage.findByEvent(event)
    }

    fun addParticipant(id: String, account: Account?, request: ParticipantAddRequest): SharedParticipateResponse? {
        val share = storage.get(id) ?: return null
        if (!share.published) return null

        val event = eventService.get(share.eventId) ?: return null
        val actor = account ?: accountService.getSystemAccount()
        val registration = registrationService.findInfoByEvent(event) ?: return null
        val response = registrationService.addParticipant(actor, registration.registration.id, request) ?: return null
        return response.toSharedParticipateResponse(registration)
    }

    private fun ParticipateResponse.toSharedParticipateResponse(registration: RegistrationInfo): SharedParticipateResponse {
        return SharedParticipateResponse(
            registration.toSharedRegistration(),
            participant, status, created
        )
    }
}

