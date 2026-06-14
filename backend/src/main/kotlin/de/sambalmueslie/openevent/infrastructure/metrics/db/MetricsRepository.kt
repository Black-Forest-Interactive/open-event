package de.sambalmueslie.openevent.infrastructure.metrics.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import java.time.LocalDate

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface MetricsRepository : PageableRepository<MetricsData, String> {

    fun deleteByTimestampLessThan(timestamp: LocalDate): Long

}