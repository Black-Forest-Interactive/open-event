package de.sambalmueslie.openevent.infrastructure.metrics.api

import de.sambalmueslie.openevent.common.BusinessObject
import java.time.LocalDate


data class DailyMetrics(
    override val id: String,
    val resource: Long,
    val action: String,
    val timestamp: LocalDate,
    val totalCount: Int,
    val uniqueCount: Int,
    val entries: List<MetricsEntry>
) : BusinessObject<String>

