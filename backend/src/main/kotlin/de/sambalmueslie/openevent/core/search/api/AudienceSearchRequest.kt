package de.sambalmueslie.openevent.core.search.api

data class AudienceSearchRequest(
    val fullTextSearch: String,
) : SearchRequest
