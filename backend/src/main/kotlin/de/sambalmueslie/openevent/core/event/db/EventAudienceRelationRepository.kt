package de.sambalmueslie.openevent.core.event.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventAudienceRelationRepository : GenericRepository<EventAudienceRelation, Long> {

    fun findByAudienceIdAndEventId(audienceId: Long, eventId: Long): EventAudienceRelation?
    fun existsByAudienceIdAndEventId(audienceId: Long, eventId: Long): Boolean
    fun findByEventId(eventId: Long, pageable: Pageable): Page<EventAudienceRelation>
    fun findByEventId(eventId: Long): List<EventAudienceRelation>
    fun findByEventIdIn(eventIds: Set<Long>): List<EventAudienceRelation>

    fun deleteByAudienceId(audienceId: Long)
    fun deleteByAudienceIdAndEventId(audienceId: Long, eventId: Long)
    fun save(relation: EventAudienceRelation): EventAudienceRelation
    fun saveAll(relations: List<EventAudienceRelation>): List<EventAudienceRelation>
    fun deleteByEventId(eventId: Long)


}
