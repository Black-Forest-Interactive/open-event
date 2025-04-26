package de.sambalmueslie.openevent.core.search.api

import de.sambalmueslie.openevent.core.category.api.Category
import io.micronaut.data.model.Page

data class CategorySearchResponse(
    override val result: Page<Category>
) : SearchResponse<Category>
