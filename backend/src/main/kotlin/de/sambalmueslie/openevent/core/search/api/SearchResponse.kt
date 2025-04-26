package de.sambalmueslie.openevent.core.search.api

import io.micronaut.data.model.Page

interface SearchResponse<T> {
    val result: Page<T>
}