package de.sambalmueslie.openevent.core.event.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventUpdateTextRequest
import de.sambalmueslie.openevent.core.history.db.HistoryStorageService
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
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
    private val audienceRelationService: EventAudienceRelationService,
    private val bookmarkRelationService: EventBookmarkRelationService,
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

    override fun setCategories(event: Event, categories: List<Category>) {
        categoryRelationService.set(event, categories)
    }

    override fun assign(event: Event, category: Category) {
        categoryRelationService.assign(event, category)
    }

    override fun revoke(event: Event, category: Category) {
        categoryRelationService.revoke(event, category)
    }

    override fun getCategories(event: Event): List<Category> {
        return categoryRelationService.get(event)
    }


    override fun setAudiences(event: Event, audiences: List<Audience>) {
        audienceRelationService.set(event, audiences)
    }

    override fun assign(event: Event, audience: Audience) {
        audienceRelationService.assign(event, audience)
    }

    override fun revoke(event: Event, audience: Audience) {
        audienceRelationService.revoke(event, audience)
    }

    override fun getAudiences(event: Event): List<Audience> {
        return audienceRelationService.get(event)
    }

    override fun assign(event: Event, announcement: Announcement) {
        announcementRelationService.assign(event, announcement)
    }

    override fun revoke(event: Event, announcement: Announcement) {
        announcementRelationService.revoke(event, announcement)
    }

    override fun getAnnouncements(event: Event, pageable: Pageable): Page<Announcement> {
        return announcementRelationService.get(event, pageable)
    }

    override fun setBookmarked(event: Event, account: Account) {
        bookmarkRelationService.assign(event, account)
    }

    override fun clearBookmarked(event: Event, account: Account) {
        bookmarkRelationService.revoke(event, account)
    }


    override fun isBookmarked(event: Event, account: Account): Boolean {
        return bookmarkRelationService.get(event, account) != null
    }

    override fun getBookmarks(id: Long): List<EventBookmarkRelation> {
        return bookmarkRelationService.get(id)
    }

    override fun getBookmarks(ids: Set<Long>): List<EventBookmarkRelation> {
        return bookmarkRelationService.get(ids)
    }

    override fun getBookmarked(account: Account, eventIds: Set<Long>): Set<Long> {
        return bookmarkRelationService.get(account, eventIds)
    }

    override fun setPublished(id: Long, value: PatchRequest<Boolean>): Event? {
        return patchData(id) { it.setPublished(value.value, timeProvider.now()) }
    }

    override fun setFeatured(id: Long, value: PatchRequest<Boolean>): Event? {
        return patchData(id) { it.setFeatured(value.value, timeProvider.now()) }
    }

    override fun setTitle(id: Long, value: PatchRequest<String>): Event? {
        return patchData(id) { it.setTitle(value.value, timeProvider.now()) }
    }

    override fun setShortText(id: Long, value: PatchRequest<String>): Event? {
        return patchData(id) { it.setShortText(value.value, timeProvider.now()) }
    }

    override fun setLongText(id: Long, value: PatchRequest<String>): Event? {
        return patchData(id) { it.setLongText(value.value, timeProvider.now()) }
    }

    override fun setTags(id: Long, value: PatchRequest<Set<String>>): Event? {
        return patchData(id) { it.setTags(value.value, timeProvider.now()) }
    }

    override fun setText(id: Long, request: EventUpdateTextRequest): Event? {
        return patchData(id) { it.setText(request, timeProvider.now()) }
    }

    override fun getAllForAccount(account: Account, pageable: Pageable): Page<Event> {
        return repository.findForUser(account.id, pageable).let { converter.convert(it) }
    }

    override fun getCategoriesByEventIds(eventIds: Set<Long>): Map<Long, List<Category>> {
        return categoryRelationService.getByEventIds(eventIds)
    }

    override fun getAudiencesByEventIds(eventIds: Set<Long>): Map<Long, List<Audience>> {
        return audienceRelationService.getByEventIds(eventIds)
    }

    override fun getOwned(owner: Account, pageable: Pageable): Page<Event> {
        return repository.findByOwnerId(owner.id, pageable).let { converter.convert(it) }
    }

    override fun getAll(pageable: Pageable): Page<Event> {
        return repository.findAllOrderByStart(pageable).let { converter.convert(it) }
    }

    override fun deleteDependencies(data: EventData) {
        announcementRelationService.delete(data)
        categoryRelationService.delete(data)
        audienceRelationService.delete(data)
        bookmarkRelationService.delete(data)
        historyStorageService.deleteByEvent(data)
    }


}
