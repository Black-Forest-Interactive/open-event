package de.sambalmueslie.openevent.core.search.api

data class AccountSearchRequest(
    val fullTextSearch: String,
) : SearchRequest
