package de.sambalmueslie.openevent.storage.location


import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface LocationRepository : PageableRepository<LocationData, Long> {
    fun findByEventId(eventId: Long): LocationData?
    fun findByEventIdIn(eventIds: Set<Long>): List<LocationData>
}
