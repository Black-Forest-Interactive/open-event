package de.sambalmueslie.openevent.storage.event


import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventRepository : PageableRepository<EventData, Long> {
    fun findByOwnerIdOrPublishedTrue(ownerId: Long, pageable: Pageable): Page<EventData>
}
