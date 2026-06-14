package de.sambalmueslie.openevent.core.search.api

import io.micronaut.data.model.Page

interface SearchResponse<T: Any> {
    val result: Page<T>
}