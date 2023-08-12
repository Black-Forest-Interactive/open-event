package de.sambalmueslie.openevent.storage.event


import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface EventRepository : DataObjectRepository<Long, EventData> {
    fun findByOwnerIdOrPublishedTrue(ownerId: Long, pageable: Pageable): Page<EventData>
}
