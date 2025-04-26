package de.sambalmueslie.openevent.core.search.api

import java.time.LocalDate


data class EventSearchRequest(
    val fullTextSearch: String,
    val from: LocalDate?,
    val to: LocalDate?,
    val ownEvents: Boolean,
    val participatingEvents: Boolean,
    val onlyAvailableEvents: Boolean
) : SearchRequest
