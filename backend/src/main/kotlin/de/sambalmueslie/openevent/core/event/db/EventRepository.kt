package de.sambalmueslie.openevent.core.event.db


import de.sambalmueslie.openevent.common.DataObjectRepository
import de.sambalmueslie.openevent.core.event.api.EventStatus
import io.micronaut.data.annotation.Query
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import java.time.LocalDateTime

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventRepository : DataObjectRepository<Long, EventData> {
    fun findByOwnerIdOrStatus(ownerId: Long, status: EventStatus, pageable: Pageable): Page<EventData>
    fun findByOwnerIdOrStatusOrderByStart(ownerId: Long, status: EventStatus, pageable: Pageable): Page<EventData>
    fun findAllOrderByStart(pageable: Pageable): Page<EventData>

    @Query(
        value = "select e.* from event e WHERE (e.owner_id = :ownerId or e.published = true) AND DATE(start) >= CURRENT_DATE ORDER BY e.start",
        countQuery = "select COUNT(e.*) from event e WHERE (e.owner_id = :ownerId or e.published = true) AND DATE(start) >= CURRENT_DATE"

    )
    fun findForUser(ownerId: Long, pageable: Pageable): Page<EventData>
    fun findByOwnerId(id: Long, pageable: Pageable): Page<EventData>

    fun findByStatusAndFinishBefore(status: EventStatus, timestamp: LocalDateTime, pageable: Pageable): Page<EventData>

    fun findByTitleContainingIgnoreCaseOrderByStart(title: String, pageable: Pageable): Page<EventData>

}
