package de.sambalmueslie.openevent.core.search.api

data class CategorySearchRequest(
    val fullTextSearch: String,
) : SearchRequest
