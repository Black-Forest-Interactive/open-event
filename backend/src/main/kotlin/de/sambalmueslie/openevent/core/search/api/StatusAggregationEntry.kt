package de.sambalmueslie.openevent.core.search.api

import de.sambalmueslie.openevent.core.event.api.EventStatus

data class StatusAggregationEntry(
    val status: EventStatus,
    val amount: Long
)
