package de.sambalmueslie.openevent.core.event.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface AnnouncementRelationRepository : GenericRepository<EventAnnouncementRelation, Long> {

    fun findByAnnouncementIdAndEventId(categoryId: Long, eventId: Long): EventAnnouncementRelation?
    fun existsByAnnouncementIdAndEventId(categoryId: Long, eventId: Long): Boolean
    fun findByEventId(eventId: Long, pageable: Pageable): Page<EventAnnouncementRelation>

    fun deleteByAnnouncementId(categoryId: Long)
    fun deleteByAnnouncementIdAndEventId(categoryId: Long, eventId: Long)
    fun save(relation: EventAnnouncementRelation): EventAnnouncementRelation
    fun deleteByEventId(eventId: Long)

}
