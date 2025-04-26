package de.sambalmueslie.openevent.core.event.db

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface EventStorage : Storage<Long, Event, EventChangeRequest> {
    fun create(request: EventChangeRequest, owner: Account): Event

    fun set(event: Event, categories: List<Category>)
    fun assign(event: Event, category: Category)
    fun revoke(event: Event, category: Category)
    fun getCategories(event: Event): List<Category>

    fun assign(event: Event, announcement: Announcement)
    fun revoke(event: Event, announcement: Announcement)
    fun getAnnouncements(event: Event, pageable: Pageable): Page<Announcement>

    fun setPublished(id: Long, value: PatchRequest<Boolean>): Event?

    @Deprecated("use opensearch for that")
    fun getAllForAccount(account: Account, pageable: Pageable): Page<Event>
    fun getCategoriesByEventIds(eventIds: Set<Long>): Map<Long, List<Category>>
    fun getOwned(owner: Account, pageable: Pageable): Page<Event>


}
