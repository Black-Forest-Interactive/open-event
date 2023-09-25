package de.sambalmueslie.openevent.storage.history


import de.sambalmueslie.openevent.core.model.HistoryEntrySource
import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface HistoryEntryRepository : DataObjectRepository<Long, HistoryEntryData> {

    fun findAllOrderByTimestampDesc(pageable: Pageable): Page<HistoryEntryData>

    fun findByEventIdOrderByTimestampDesc(eventId: Long, pageable: Pageable): Page<HistoryEntryData>
    fun findByEventIdAndActorIdOrSourceInOrderByTimestampDesc(
        eventId: Long,
        actorId: Long,
        source: Set<HistoryEntrySource>,
        pageable: Pageable
    ): Page<HistoryEntryData>

    @Query(
        value = "SELECT h.* FROM history_entry INNER JOIN event e ON e.id = h.event_id WHERE (e.owner_id = :actorId) OR (h.actor_id = :actorId) OR (h.source in (:source)) ORDER BY h.timestamp DESC",
        countQuery = "SELECT h.* FROM history_entry INNER JOIN event e ON e.id = h.event_id WHERE (e.owner_id = :actorId) OR (h.actor_id = :actorId) OR (h.source in (:source))"
    )
    fun findAllForAccount(actorId: Long,source: Set<HistoryEntrySource>, pageable: Pageable): Page<HistoryEntryData>
    fun deleteByEventId(id: Long)
}
