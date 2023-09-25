package de.sambalmueslie.openevent.storage.event

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.GenericRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface CategoryRelationRepository : GenericRepository<EventCategoryRelation, Long> {

    fun findByCategoryIdAndEventId(categoryId: Long, eventId: Long): EventCategoryRelation?
    fun existsByCategoryIdAndEventId(categoryId: Long, eventId: Long): Boolean
    fun findByEventId(eventId: Long, pageable: Pageable): Page<EventCategoryRelation>
    fun findByEventId(eventId: Long): List<EventCategoryRelation>
    fun findByEventIdIn(eventIds: Set<Long>): List<EventCategoryRelation>

    fun deleteByCategoryId(categoryId: Long)
    fun deleteByCategoryIdAndEventId(categoryId: Long, eventId: Long)
    fun save(relation: EventCategoryRelation): EventCategoryRelation
    fun saveAll(relations: List<EventCategoryRelation>): List<EventCategoryRelation>
    fun deleteByEventId(eventId: Long)


}
