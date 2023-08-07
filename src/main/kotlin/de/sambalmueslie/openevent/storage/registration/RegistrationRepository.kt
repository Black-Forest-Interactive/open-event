package de.sambalmueslie.openevent.storage.registration


import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface RegistrationRepository : PageableRepository<RegistrationData, Long> {
    fun findByEventId(eventId: Long): RegistrationData?
    fun findByEventIdIn(eventIds: Set<Long>): List<RegistrationData>
}
