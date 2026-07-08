package de.sambalmueslie.openevent.core.event.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventAnnouncementRelationRepository : GenericRepository<EventAnnouncementRelation, Long> {

    fun findByEventIdAndAnnouncementId(eventId: Long, announcementId: Long): EventAnnouncementRelation?
    fun existsByEventIdAndAnnouncementId(eventId: Long, announcementId: Long): Boolean
    fun deleteByEventIdAndAnnouncementId(eventId: Long, announcementId: Long)



    fun findByEventId(eventId: Long, pageable: Pageable): Page<EventAnnouncementRelation>
    fun findByAnnouncementId(announcementId: Long): EventAnnouncementRelation?

    fun save(relation: EventAnnouncementRelation): EventAnnouncementRelation
    fun deleteByEventId(eventId: Long)

}
