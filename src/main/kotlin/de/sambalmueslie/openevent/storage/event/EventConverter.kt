package de.sambalmueslie.openevent.storage.event


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.error.InconsistentDataException
import de.sambalmueslie.openevent.storage.DataObjectConverter
import de.sambalmueslie.openevent.storage.account.AccountStorageService
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class EventConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Event, EventData> {

    override fun convert(obj: EventData): Event {
        return convert(obj, accountService.get(obj.ownerId))
    }

    override fun convert(objs: List<EventData>): List<Event> {
        val ownerIds = objs.map { it.ownerId }.toSet()
        val author = accountService.getByIds(ownerIds).associateBy { it.id }
        return objs.map { convert(it, author[it.ownerId]) }
    }

    override fun convert(page: Page<EventData>): Page<Event> {
        val ownerIds = page.content.map { it.ownerId }.toSet()
        val author = accountService.getByIds(ownerIds).associateBy { it.id }
        return page.map { convert(it, author[it.ownerId]) }
    }

    private fun convert(data: EventData, author: Account?): Event {
        if (author == null) throw InconsistentDataException("Cannot find author for event")
        return data.convert(author)
    }
}
