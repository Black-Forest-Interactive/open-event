package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import java.time.LocalDateTime

interface HistoryEntryStorage : Storage<Long, HistoryEntry, HistoryEntryChangeRequest> {
    fun create(request: HistoryEntryChangeRequest, event: Event, actor: Account): HistoryEntry
    fun create(request: HistoryEntryChangeRequest, event: Event, actor: Account, timestamp: LocalDateTime): HistoryEntry
    fun findByEvent(event: Event, pageable: Pageable): Page<HistoryEntry>

    fun findByEventAndActorOrSource(
        event: Event,
        account: Account,
        source: Set<HistoryEntrySource>,
        pageable: Pageable
    ): Page<HistoryEntry>

    fun getAllForAccountOrSource(
        account: Account,
        source: Set<HistoryEntrySource>,
        pageable: Pageable
    ): Page<HistoryEntry>

    fun deleteAll()
}
