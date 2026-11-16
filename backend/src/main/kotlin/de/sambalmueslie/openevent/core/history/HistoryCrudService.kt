package de.sambalmueslie.openevent.core.history


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.history.api.HistoryEntry
import de.sambalmueslie.openevent.core.history.api.HistoryEntryChangeRequest
import de.sambalmueslie.openevent.core.history.api.HistoryEntrySource
import de.sambalmueslie.openevent.core.history.api.HistoryEventInfo
import de.sambalmueslie.openevent.core.history.db.HistoryEntryStorage
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.event.EventSearchOperator
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class HistoryCrudService(
    private val eventService: EventCrudService,
    private val eventSearchOperator: EventSearchOperator,
    private val storage: HistoryEntryStorage
) : BaseCrudService<Long, HistoryEntry, HistoryEntryChangeRequest, HistoryEntryChangeListener>(storage) {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(HistoryCrudService::class.java)
        private val source = setOf(HistoryEntrySource.EVENT)
    }

    fun create(actor: Account, event: Event, request: HistoryEntryChangeRequest): HistoryEntry {
        isValid(request)
        val result = storage.create(request, event, actor)
        notifyCreated(actor, result)
        return result
    }

    override fun isValid(request: HistoryEntryChangeRequest) {
        // intentionally left empty
    }

    fun getForEvent(eventId: Long, pageable: Pageable): Page<HistoryEntry> {
        val event = eventService.get(eventId) ?: return Page.empty()
        return storage.findByEvent(event, pageable)
    }

    fun getForEventAndAccount(eventId: Long, account: Account, pageable: Pageable): Page<HistoryEntry> {
        val event = eventService.get(eventId) ?: return Page.empty()
        return if (event.owner.id == account.id) {
            storage.findByEvent(event, pageable)
        } else {
            storage.findByEventAndActorOrSource(event, account, source, pageable)
        }
    }

    fun getAllForAccount(account: Account, pageable: Pageable): Page<HistoryEntry> {
        return storage.getAllForAccountOrSource(account, source, pageable)
    }

    fun getAllInfos(account: Account, request: EventSearchRequest, pageable: Pageable): Page<HistoryEventInfo> {
        val events = eventSearchOperator.search(account, request, pageable)
        val content = events.result
        val eventIds = content.mapNotNull { it.id.toLongOrNull() }.toSet()
        val entries = storage.findByEvents(eventIds).groupBy { it.eventId }
        val result = content.content.map { HistoryEventInfo(it, entries[it.id.toLong()] ?: emptyList()) }
        return Page.of(result, content.pageable, content.totalSize)
    }


}
