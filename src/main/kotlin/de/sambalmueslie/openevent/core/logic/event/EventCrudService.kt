package de.sambalmueslie.openevent.core.logic.event


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.logic.account.AccountCrudService
import de.sambalmueslie.openevent.core.logic.category.CategoryCrudService
import de.sambalmueslie.openevent.core.logic.location.LocationCrudService
import de.sambalmueslie.openevent.core.logic.registration.RegistrationCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.EventStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventCrudService(
    private val accountCrudService: AccountCrudService,
    private val storage: EventStorage,
    private val locationCrudService: LocationCrudService,
    private val registrationCrudService: RegistrationCrudService,
    private val categoryCrudService: CategoryCrudService
) : BaseCrudService<Long, Event, EventChangeRequest>(storage, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventCrudService::class.java)
    }

    fun create(request: EventChangeRequest, auth: Authentication): Event {
        val account = accountCrudService.get(auth) ?: throw InvalidRequestException("Cannot find account")
        return create(request, account)
    }

    fun create(request: EventChangeRequest, owner: Account): Event {
        val result = storage.create(request, owner)
        notifyCreated(result)
        val categories = categoryCrudService.getByIds(request.categoryIds)
        if (categories.isNotEmpty()) storage.set(result, categories)
        request.location?.let { locationCrudService.create(result, it) }
        registrationCrudService.create(result, request.registration)
        return result
    }

    override fun update(id: Long, request: EventChangeRequest): Event {
        val result = super.update(id, request)
        val categories = categoryCrudService.getByIds(request.categoryIds)
        if (categories.isNotEmpty()) storage.set(result, categories)
        if (request.location == null) {
            locationCrudService.deleteByEvent(result)
        } else {
            locationCrudService.updateByEvent(result, request.location)
        }
        registrationCrudService.updateByEvent(result, request.registration)
        return result
    }

    override fun delete(id: Long): Event? {
        val event = storage.get(id) ?: return null
        locationCrudService.deleteByEvent(event)
        registrationCrudService.deleteByEvent(event)
        return super.delete(id)
    }

    fun setPublished(id: Long, value: PatchRequest<Boolean>): Event? {
        return storage.setPublished(id, value)
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

    fun getInfo(id: Long): EventInfo? {
        val event = get(id) ?: return null
        val location = locationCrudService.findByEvent(event)
        val registration = registrationCrudService.findInfoByEvent(event)
        val categories = storage.getCategories(event)
        return EventInfo(event, location, registration, categories)
    }

    fun getInfos(pageable: Pageable): Page<EventInfo> {
        return convertInfo(getAll(pageable))
    }

    fun getAllForAccount(account: Account, pageable: Pageable): Page<Event> {
        return storage.getAllForAccount(account, pageable)
    }

    fun getInfosForAccount(account: Account, pageable: Pageable): Page<EventInfo> {
        return convertInfo(getAllForAccount(account, pageable))
    }

    internal fun convertInfo(events: Page<Event>): Page<EventInfo> {
        val eventIds = events.content.map { it.id }.toSet()
        val locations = locationCrudService.findByEventIds(eventIds).associateBy { it.id }
        val registrations = registrationCrudService.findInfosByEventIds(eventIds).associateBy { it.registration.id }
        val categories = storage.getCategoriesByEventIds(eventIds)
        return events.map { EventInfo(it, locations[it.id], registrations[it.id], categories[it.id] ?: emptyList()) }
    }



}
