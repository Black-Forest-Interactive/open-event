package de.sambalmueslie.openevent.core.event


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PageableSequence
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.location.LocationCrudService
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.logic.account.api.Account
import de.sambalmueslie.openevent.core.logic.event.api.Event
import de.sambalmueslie.openevent.core.logic.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.logic.event.api.EventInfo
import de.sambalmueslie.openevent.core.logic.event.api.EventStats
import de.sambalmueslie.openevent.core.logic.event.db.EventStorage
import de.sambalmueslie.openevent.core.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.registration.api.Registration
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventCrudService(
    private val storage: EventStorage,
    private val locationCrudService: LocationCrudService,
    private val registrationCrudService: RegistrationCrudService,
    private val categoryCrudService: CategoryCrudService
) : BaseCrudService<Long, Event, EventChangeRequest, EventChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventCrudService::class.java)
    }

    fun create(actor: Account, request: EventChangeRequest): Event {
        val result = storage.create(request, actor)
        notifyCreated(actor, result)
        val categories = categoryCrudService.getByIds(request.categoryIds)
        if (categories.isNotEmpty()) storage.set(result, categories)
        request.location?.let { locationCrudService.create(actor, result, it) }
        registrationCrudService.create(actor, result, request.registration)
        return result
    }

    override fun update(actor: Account, id: Long, request: EventChangeRequest): Event {
        val result = super.update(actor, id, request)
        val categories = categoryCrudService.getByIds(request.categoryIds)
        if (categories.isNotEmpty()) storage.set(result, categories)
        if (request.location == null) {
            locationCrudService.deleteByEvent(actor, result)
        } else {
            locationCrudService.updateByEvent(actor, result, request.location)
        }
        registrationCrudService.updateByEvent(actor, result, request.registration)
        return result
    }

    override fun delete(actor: Account, id: Long): Event? {
        val event = storage.get(id) ?: return null
        locationCrudService.deleteByEvent(actor, event)
        registrationCrudService.deleteByEvent(actor, event)
        return super.delete(actor, id)
    }

    fun setPublished(actor: Account, id: Long, value: PatchRequest<Boolean>): Event? {
        val result = storage.setPublished(id, value) ?: return null
        notify { it.publishedChanged(actor, result) }
        return result
    }

    fun getLocation(id: Long): Location? {
        val event = get(id) ?: return null
        return locationCrudService.findByEvent(event)
    }

    fun getRegistration(id: Long): Registration? {
        val event = get(id) ?: return null
        return registrationCrudService.findByEvent(event)
    }

    fun getCategories(id: Long): List<Category> {
        val event = get(id) ?: return emptyList()
        return storage.getCategories(event)
    }

    fun getInfo(id: Long, account: Account): EventInfo? {
        val event = get(id) ?: return null
        val location = locationCrudService.findByEvent(event)
        val registration = registrationCrudService.findInfoByEvent(event)
        val categories = storage.getCategories(event)
        val canEdit = event.owner.id == account.id
        return EventInfo(event, location, registration, categories, canEdit)
    }

    fun getInfos(pageable: Pageable): Page<EventInfo> {
        return convertInfo(getAll(pageable))
    }

    fun getAllForAccount(account: Account, pageable: Pageable): Page<Event> {
        return storage.getAllForAccount(account, pageable)
    }

    fun getInfosForAccount(account: Account, pageable: Pageable): Page<EventInfo> {
        return convertInfo(getAllForAccount(account, pageable), account)
    }

    internal fun convertInfo(events: Page<Event>, account: Account? = null): Page<EventInfo> {
        val eventIds = events.content.map { it.id }.toSet()
        val locations = locationCrudService.findByEventIds(eventIds).associateBy { it.eventId }
        val registrations = registrationCrudService.findInfosByEventIds(eventIds).associateBy { it.registration.id }
        val categories = storage.getCategoriesByEventIds(eventIds)
        val canEdit = events.content.associate { it.id to (it.owner.id == account?.id) }
        return events.map {
            EventInfo(
                it,
                locations[it.id],
                registrations[it.id],
                categories[it.id] ?: emptyList(),
                canEdit[it.id] ?: false
            )
        }
    }

    fun getStats(): List<EventStats> {
        return PageableSequence { getInfos(it) }.mapNotNull { convertStats(it) }.toList()
    }

    private fun convertStats(info: EventInfo): EventStats? {
        val registration = info.registration ?: return null

        val totalAmount = registration.registration.maxGuestAmount

        var participantsSize = 0L
        var participantsAmount = 0L
        var waitingListSize = 0L
        var waitingListAmount = 0L

        registration.participants.forEach { p ->
            if (p.waitingList) {
                waitingListSize++
                waitingListAmount += p.size
            } else {
                participantsSize++
                participantsAmount += p.size
            }
        }

        val isFull = totalAmount in 1..participantsAmount
        val isEmpty = participantsSize <= 0
        return EventStats(
            info.event,
            isFull,
            isEmpty,
            participantsSize,
            participantsAmount,
            waitingListSize,
            waitingListAmount
        )
    }


}
