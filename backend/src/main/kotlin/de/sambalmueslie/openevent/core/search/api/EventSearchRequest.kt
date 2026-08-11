package de.sambalmueslie.openevent.core.search.api

import de.sambalmueslie.openevent.core.event.api.EventStatus
import java.time.LocalDate


data class EventSearchRequest(
    val fullTextSearch: String,
    val from: LocalDate?,
    val to: LocalDate?,
    val ownEvents: Boolean,
    val featured: Boolean,
    val bookmarked: Boolean,
    val participatingEvents: Boolean,
    val onlyAvailableEvents: Boolean,
    val categories: Set<String> = emptySet(),
    val audiences: Set<String> = emptySet(),
    val status: Set<EventStatus> = emptySet()
) : SearchRequest
