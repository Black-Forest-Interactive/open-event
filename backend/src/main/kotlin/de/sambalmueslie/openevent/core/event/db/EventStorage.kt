package de.sambalmueslie.openevent.core.event.db

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.event.api.EventUpdateTextRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface EventStorage : Storage<Long, Event, EventChangeRequest> {
    fun create(request: EventChangeRequest, owner: Account): Event

    fun setCategories(event: Event, categories: List<Category>)
    fun assign(event: Event, category: Category)
    fun revoke(event: Event, category: Category)
    fun getCategories(event: Event): List<Category>

    fun setAudiences(event: Event, audiences: List<Audience>)
    fun assign(event: Event, audience: Audience)
    fun revoke(event: Event, audience: Audience)
    fun getAudiences(event: Event): List<Audience>

    fun add(event: Event, announcement: Announcement)
    fun remove(event: Event, announcement: Announcement)
    fun isAssigned(event: Event, announcement: Announcement): Boolean
    fun getAnnouncements(event: Event, pageable: Pageable): Page<Announcement>


    fun setBookmarked(event: Event, account: Account)
    fun clearBookmarked(event: Event, account: Account)
    fun isBookmarked(event: Event, account: Account): Boolean
    fun getBookmarks(id: Long): List<EventBookmarkRelation>
    fun getBookmarks(ids: Set<Long>): List<EventBookmarkRelation>
    fun getBookmarked(account: Account, eventIds: Set<Long>): Set<Long>

    fun setStatus(id: Long, value: EventStatus): Event?
    fun setPublished(id: Long, value: PatchRequest<Boolean>): Event?
    fun setFeatured(id: Long, value: PatchRequest<Boolean>): Event?
    fun setTitle(id: Long, value: PatchRequest<String>): Event?
    fun setShortText(id: Long, value: PatchRequest<String>): Event?
    fun setLongText(id: Long, value: PatchRequest<String>): Event?
    fun setTags(id: Long, value: PatchRequest<Set<String>>): Event?
    fun setText(id: Long, request: EventUpdateTextRequest): Event?

    @Deprecated("use opensearch for that")
    fun getAllForAccount(account: Account, pageable: Pageable): Page<Event>
    fun getCategoriesByEventIds(eventIds: Set<Long>): Map<Long, List<Category>>
    fun getAudiencesByEventIds(eventIds: Set<Long>): Map<Long, List<Audience>>
    fun getOwned(owner: Account, pageable: Pageable): Page<Event>
    fun getAll(title: String, pageable: Pageable): Page<Event>


}
