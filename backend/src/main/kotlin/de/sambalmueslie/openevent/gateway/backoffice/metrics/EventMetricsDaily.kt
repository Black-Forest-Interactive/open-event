package de.sambalmueslie.openevent.gateway.backoffice.metrics

import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.infrastructure.metrics.api.DailyMetrics

data class EventMetricsDaily(
    val event: EventInfo,
    val metrics: List<DailyMetrics>
)
