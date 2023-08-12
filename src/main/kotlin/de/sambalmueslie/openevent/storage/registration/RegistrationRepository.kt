package de.sambalmueslie.openevent.storage.registration


import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface RegistrationRepository : DataObjectRepository<Long, RegistrationData> {
    fun findByEventId(eventId: Long): RegistrationData?
    fun findByEventIdIn(eventIds: Set<Long>): List<RegistrationData>
}
