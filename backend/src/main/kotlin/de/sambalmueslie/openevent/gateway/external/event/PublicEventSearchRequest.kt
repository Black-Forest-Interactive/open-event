package de.sambalmueslie.openevent.gateway.external.event

import java.time.LocalDate

data class PublicEventSearchRequest(
    val fullTextSearch: String,
    val from: LocalDate?,
    val to: LocalDate?,
    val onlyAvailableEvents: Boolean
)
