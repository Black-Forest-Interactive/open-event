package de.sambalmueslie.openevent.storage.location


import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface LocationRepository : DataObjectRepository<Long, LocationData> {
    fun findByEventId(eventId: Long): LocationData?
    fun findByEventIdIn(eventIds: Set<Long>): List<LocationData>
}
