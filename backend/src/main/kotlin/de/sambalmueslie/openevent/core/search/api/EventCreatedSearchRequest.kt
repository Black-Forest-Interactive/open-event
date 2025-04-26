package de.sambalmueslie.openevent.core.search.api

import java.time.LocalDateTime

data class EventCreatedSearchRequest(
    val from: LocalDateTime?,
    val to: LocalDateTime?,
    val onlyPublishedEvents: Boolean,
    val onlyAvailableEvents: Boolean
) : SearchRequest