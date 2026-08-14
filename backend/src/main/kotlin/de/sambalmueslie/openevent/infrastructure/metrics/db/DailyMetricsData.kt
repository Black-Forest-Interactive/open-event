package de.sambalmueslie.openevent.infrastructure.metrics.db

import de.sambalmueslie.openevent.infrastructure.metrics.api.DailyMetrics
import io.micronaut.data.annotation.TypeDef
import io.micronaut.data.model.DataType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity(name = "MetricsDaily")
@Table(name = "metrics_daily")
data class DailyMetricsData(
    @Id var id: String,
    @Column var type: String,
    @Column var resource: Long,
    @Column var action: String,
    @Column var timestamp: LocalDate,
    @Column var totalCount: Int,
    @Column var uniqueCount: Int,
    @Column @field:TypeDef(type = DataType.JSON) var entries: MutableList<MetricsEntryData> ,
) {
    fun convert(): DailyMetrics {
        return DailyMetrics(id, resource, action, timestamp, totalCount, uniqueCount, entries.map { it.convert() })
    }
}
