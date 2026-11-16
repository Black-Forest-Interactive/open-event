package de.sambalmueslie.openevent.gateway.backoffice.metrics

import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.infrastructure.metrics.api.WeeklyMetrics

data class EventMetricsWeekly(
    val event: EventInfo,
    val metrics: List<WeeklyMetrics>
)
