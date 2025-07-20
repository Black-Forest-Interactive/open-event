package de.sambalmueslie.openevent.infrastructure.metrics.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate

@Entity(name = "Metrics")
@Table(name = "metrics")
data class MetricsData(
    @Id var id: String,
    @Column var source: String,
    @Column var resource: Long,
    @Column var action: String,
    @Column var timestamp: LocalDate,
    @Column var totalCount: Int,
    @Column var uniqueCount: Int,
) {
    fun update(data: MetricsData): MetricsData {
        totalCount += data.totalCount
        uniqueCount += data.uniqueCount
        return data
    }
}
