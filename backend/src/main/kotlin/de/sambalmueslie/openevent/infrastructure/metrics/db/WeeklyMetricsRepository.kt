package de.sambalmueslie.openevent.infrastructure.metrics.db

import io.micronaut.data.annotation.Repository
import io.micronaut.data.jdbc.annotation.JdbcRepository
import io.micronaut.data.model.query.builder.sql.Dialect
import io.micronaut.data.repository.PageableRepository
import java.time.LocalDate

@Repository
@JdbcRepository(dialect = Dialect.POSTGRES)
interface WeeklyMetricsRepository : PageableRepository<WeeklyMetricsData, String> {

    fun deleteByTimestampLessThan(timestamp: LocalDate): Long

    fun findByTypeAndActionAndTimestampBetween(type: String, action: String,  from: LocalDate, to: LocalDate): List<WeeklyMetricsData>
    fun findByTypeAndActionAndResourceAndTimestampBetween(type: String, action: String, resource: Long, from: LocalDate, to: LocalDate): List<WeeklyMetricsData>

}