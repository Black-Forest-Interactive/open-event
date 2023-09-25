package de.sambalmueslie.openevent.storage.event


import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.EventStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.BaseStorageService
import de.sambalmueslie.openevent.storage.history.HistoryStorageService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventStorageService(
    private val repository: EventRepository,
    private val converter: EventConverter,

    private val categoryRelationService: EventCategoryRelationService,
    private val announcementRelationService: EventAnnouncementRelationService,
    private val historyStorageService: HistoryStorageService,

    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Event, EventChangeRequest, EventData>(
    repository,
    converter,
    cacheService,
    Event::class,
    logger
), EventStorage {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventStorageService::class.java)
        private const val OWNER_REFERENCE = "owner"
    }

    override fun create(request: EventChangeRequest, owner: Account): Event {
        return create(request, mapOf(Pair(OWNER_REFERENCE, owner)))
    }

    override fun createData(request: EventChangeRequest, properties: Map<String, Any>): EventData {
        val owner = properties[OWNER_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        return EventData.create(owner, request, timeProvider.now())
    }

    override fun updateData(data: EventData, request: EventChangeRequest): EventData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: EventChangeRequest) {
        if (request.title.isBlank()) throw InvalidRequestException("Title cannot be blank")
    }

    override fun assign(event: Event, category: Category) {
        categoryRelationService.assign(event, category)
    }

    override fun assign(event: Event, announcement: Announcement) {
        announcementRelationService.assign(event, announcement)
    }

    override fun revoke(event: Event, category: Category) {
        categoryRelationService.revoke(event, category)
    }

    override fun revoke(event: Event, announcement: Announcement) {
        announcementRelationService.revoke(event, announcement)
    }

    override fun set(event: Event, categories: List<Category>) {
        categoryRelationService.set(event, categories)
    }

    override fun getCategories(event: Event): List<Category> {
        return categoryRelationService.get(event)
    }

    override fun getCategoriesByEventIds(eventIds: Set<Long>): Map<Long, List<Category>> {
        return categoryRelationService.getByEventIds(eventIds)
    }

    override fun deleteDependencies(data: EventData) {
        announcementRelationService.delete(data)
        categoryRelationService.delete(data)
        historyStorageService.deleteByEvent(data)
    }

    override fun getAnnouncements(event: Event, pageable: Pageable): Page<Announcement> {
        return announcementRelationService.get(event, pageable)
    }

    override fun setPublished(id: Long, value: PatchRequest<Boolean>): Event? {
        return patchData(id) { it.setPublished(value.value, timeProvider.now()) }
    }

    override fun getAllForAccount(account: Account, pageable: Pageable): Page<Event> {
        return repository.findForUser(account.id, pageable).let { converter.convert(it) }
    }

    override fun getAll(pageable: Pageable): Page<Event> {
        return repository.findAllOrderByStart(pageable).let { converter.convert(it) }
    }


}
