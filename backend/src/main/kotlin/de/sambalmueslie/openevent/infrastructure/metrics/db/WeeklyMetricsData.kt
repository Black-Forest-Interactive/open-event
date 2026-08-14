package de.sambalmueslie.openevent.infrastructure.metrics.db

import de.sambalmueslie.openevent.infrastructure.metrics.api.WeeklyMetrics
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity(name = "MetricsWeekly")
@Table(name = "metrics_weekly")
data class WeeklyMetricsData(
    @Id var id: String,
    @Column var type: String,
    @Column var resource: Long,
    @Column var action: String,
    @Column var timestamp: LocalDate,
    @Column var totalCount: Int,
    @Column var uniqueCount: Int,
    @Column @field:TypeDef(type = DataType.JSON) var entries: List<MetricsEntryData> = emptyList(),
    @Column @field:TypeDef(type = DataType.JSON) var dailyBreakdown: List<DailyBreakdownEntryData> = emptyList(),
) {
    fun convert(): WeeklyMetrics {
        return WeeklyMetrics(id, resource, action, timestamp, totalCount, uniqueCount, entries.map { it.convert() }, dailyBreakdown.map { it.convert() })
    }
}
