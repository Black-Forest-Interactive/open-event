package de.sambalmueslie.openevent.infrastructure.metrics.api

import java.time.LocalDate

data class DailyBreakdownEntry(
    val day: LocalDate,
    val entries: List<MetricsEntry>
)
