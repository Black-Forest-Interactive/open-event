package de.sambalmueslie.openevent.core.event.db


import de.sambalmueslie.openevent.common.DataObjectConverter
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.account.db.AccountStorageService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.error.InconsistentDataException
import io.micronaut.data.model.Page
import jakarta.inject.Singleton

@Singleton
class EventConverter(
    private val accountService: AccountStorageService,
) : DataObjectConverter<Event, EventData> {

    override fun convert(obj: EventData): Event {
        return convert(obj, accountService.getInfo(obj.ownerId))
    }

    override fun convert(objs: List<EventData>): List<Event> {
        val ownerIds = objs.map { it.ownerId }.toSet()
        val author = accountService.getInfoByIds(ownerIds).associateBy { it.id }
        return objs.map { convert(it, author[it.ownerId]) }
    }

    override fun convert(page: Page<EventData>): Page<Event> {
        val ownerIds = page.content.map { it.ownerId }.toSet()
        val author = accountService.getInfoByIds(ownerIds).associateBy { it.id }
        return page.map { convert(it, author[it.ownerId]) }
    }

    private fun convert(data: EventData, author: AccountInfo?): Event {
        if (author == null) throw InconsistentDataException("Cannot find author for event")
        return data.convert(author)
    }
}
