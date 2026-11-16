package de.sambalmueslie.openevent.infrastructure.metrics.db

import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import java.time.LocalDateTime

@JdbcRepository(dialect = Dialect.POSTGRES)
interface MetricsTraceRepository : PageableRepository<MetricsTraceData, Long> {
    fun deleteByTimestampLessThan(timestamp: LocalDateTime): Long

}