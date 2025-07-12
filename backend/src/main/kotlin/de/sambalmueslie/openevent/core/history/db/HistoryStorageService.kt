package de.sambalmueslie.openevent.core.history.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.db.EventData
import de.sambalmueslie.openevent.core.history.api.HistoryEntry
import de.sambalmueslie.openevent.core.history.api.HistoryEntryChangeRequest
import de.sambalmueslie.openevent.core.history.api.HistoryEntrySource
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

@Singleton
class HistoryStorageService(
    private val repository: HistoryEntryRepository,
    private val converter: HistoryEntryConverter,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, HistoryEntry, HistoryEntryChangeRequest, HistoryEntryData>(
    repository,
    converter,
    cacheService,
    HistoryEntry::class,
    logger
), HistoryEntryStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(HistoryStorageService::class.java)
        private const val EVENT_REFERENCE = "event"
        private const val ACTOR_REFERENCE = "actor"
        private const val TIMESTAMP_REFERENCE = "timestamp"
    }

    override fun create(request: HistoryEntryChangeRequest, event: Event, actor: Account): HistoryEntry {
        return create(
            request,
            mapOf(
                Pair(EVENT_REFERENCE, event),
                Pair(ACTOR_REFERENCE, actor),
                Pair(TIMESTAMP_REFERENCE, timeProvider.now())
            )
        )
    }

    override fun create(
        request: HistoryEntryChangeRequest,
        event: Event,
        actor: Account,
        timestamp: LocalDateTime
    ): HistoryEntry {
        return create(
            request,
            mapOf(Pair(EVENT_REFERENCE, event), Pair(ACTOR_REFERENCE, actor), Pair(TIMESTAMP_REFERENCE, timestamp))
        )
    }

    override fun createData(request: HistoryEntryChangeRequest, properties: Map<String, Any>): HistoryEntryData {
        val event = properties[EVENT_REFERENCE] as? Event ?: throw InvalidRequestException("Cannot find event")
        val actor = properties[ACTOR_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find actor")
        val timestamp = properties[TIMESTAMP_REFERENCE] as? LocalDateTime
            ?: throw InvalidRequestException("Cannot find timestamp")

        return HistoryEntryData.create(event, actor, request, timestamp)
    }

    override fun updateData(data: HistoryEntryData, request: HistoryEntryChangeRequest): HistoryEntryData {
        return data.update(request, timeProvider.now())
    }

    override fun getAll(pageable: Pageable): Page<HistoryEntry> {
        return repository.findAllOrderByTimestampDesc(pageable).map { converter.convert(it) }
    }

    override fun findByEvent(event: Event, pageable: Pageable): Page<HistoryEntry> {
        return repository.findByEventIdOrderByTimestampDesc(event.id, pageable).map { converter.convert(it) }
    }

    override fun findByEventAndActorOrSource(
        event: Event,
        account: Account,
        source: Set<HistoryEntrySource>,
        pageable: Pageable
    ): Page<HistoryEntry> {
        return repository.findByEventIdAndActorIdOrSourceInOrderByTimestampDesc(event.id, account.id, source, pageable)
            .map { converter.convert(it) }
    }

    override fun getAllForAccountOrSource(
        account: Account,
        source: Set<HistoryEntrySource>,
        pageable: Pageable
    ): Page<HistoryEntry> {
        return repository.findAllForAccount(account.id, source, pageable)
            .map { converter.convert(it) }
    }

    fun deleteByEvent(event: EventData) {
        repository.deleteByEventId(event.id)
    }

}
