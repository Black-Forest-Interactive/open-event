package de.sambalmueslie.openevent.infrastructure.audit.db

import de.sambalmueslie.openevent.storage.DataObjectRepository
import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.model.query.builder.sql.Dialect

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface AuditLogEntryRepository : DataObjectRepository<Long, AuditLogEntryData> {

    fun findAllOrderByTimestampDesc(pageable: Pageable): Page<AuditLogEntryData>

    fun findAllByReferenceId(referenceId: String, pageable: Pageable): Page<AuditLogEntryData>
}
