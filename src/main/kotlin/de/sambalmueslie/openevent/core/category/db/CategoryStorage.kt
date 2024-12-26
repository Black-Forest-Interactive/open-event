package de.sambalmueslie.openevent.core.category.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest

interface CategoryStorage : Storage<Long, Category, CategoryChangeRequest> {
    fun findByName(name: String): Category?
}
