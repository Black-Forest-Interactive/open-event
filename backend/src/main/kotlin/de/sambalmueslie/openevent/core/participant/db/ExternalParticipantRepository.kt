package de.sambalmueslie.openevent.core.participant.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import java.time.LocalDateTime

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface ExternalParticipantRepository : PageableRepository<ExternalParticipantData, String> {
    fun findByEventIdAndEmail(eventId: Long, email: String): ExternalParticipantData?
    fun findByExpiresLessThanEquals(timestamp: LocalDateTime, pageable: Pageable): Page<ExternalParticipantData>
}