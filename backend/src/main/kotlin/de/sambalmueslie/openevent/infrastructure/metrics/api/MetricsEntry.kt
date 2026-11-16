package de.sambalmueslie.openevent.infrastructure.metrics.api

data class MetricsEntry(
    val source: MetricsSource,
    val totalCount: Int,
    val uniqueCount: Int,
)
