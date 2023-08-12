package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.core.model.CategoryChangeRequest

interface CategoryStorage : Storage<Long, Category, CategoryChangeRequest> {
    fun findByName(name: String): Category?
}
