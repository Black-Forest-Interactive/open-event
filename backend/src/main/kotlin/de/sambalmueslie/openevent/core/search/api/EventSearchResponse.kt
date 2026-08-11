package de.sambalmueslie.openevent.core.search.api

import io.micronaut.data.model.Page

data class EventSearchResponse(
    override val result: Page<EventSearchEntry>,
    val dateHistogram: List<DateHistogramEntry>,
    val statusAggregation: List<StatusAggregationEntry>
) : SearchResponse<EventSearchEntry>
