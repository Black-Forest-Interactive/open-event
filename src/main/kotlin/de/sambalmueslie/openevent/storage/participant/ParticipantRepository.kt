package de.sambalmueslie.openevent.storage.participant


import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ParticipantRepository : PageableRepository<ParticipantData, Long> {
    fun findByIdIn(ids: Set<Long>): List<ParticipantData>
    fun findByRegistrationId(id: Long): List<ParticipantData>
}
