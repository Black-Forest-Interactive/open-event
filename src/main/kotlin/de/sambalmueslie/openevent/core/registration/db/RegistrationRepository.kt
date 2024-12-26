package de.sambalmueslie.openevent.core.registration.db


import de.sambalmueslie.openevent.common.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface RegistrationRepository : DataObjectRepository<Long, RegistrationData> {
    fun findByEventId(eventId: Long): RegistrationData?
    fun findByEventIdIn(eventIds: Set<Long>): List<RegistrationData>
}
