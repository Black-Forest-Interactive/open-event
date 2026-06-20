package de.sambalmueslie.openevent.core.event.db

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventBookmarkRelationRepository : GenericRepository<EventBookmarkRelation, Long> {

    fun findByAccountIdAndEventId(accountId: Long, eventId: Long): EventBookmarkRelation?
    fun findByAccountIdAndEventIdIn(accountId: Long, eventIds: Set<Long>): List<EventBookmarkRelation>
    fun existsByAccountIdAndEventId(accountId: Long, eventId: Long): Boolean
    fun findByEventId(eventId: Long, pageable: Pageable): Page<EventBookmarkRelation>
    fun findByEventId(eventId: Long): List<EventBookmarkRelation>
    fun findByEventIdIn(eventIds: Set<Long>): List<EventBookmarkRelation>

    fun deleteByAccountId(accountId: Long)
    fun deleteByAccountIdAndEventId(accountId: Long, eventId: Long)
    fun save(relation: EventBookmarkRelation): EventBookmarkRelation
    fun saveAll(relations: List<EventBookmarkRelation>): List<EventBookmarkRelation>
    fun deleteByEventId(eventId: Long)


}
