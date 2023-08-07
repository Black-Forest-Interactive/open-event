package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.core.model.CategoryChangeRequest
import io.micronaut.security.authentication.Authentication

interface CategoryAPI : CrudAPI<Long, Category, CategoryChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.category.read"
        const val PERMISSION_WRITE = "openevent.category.write"
    }

    fun findByName(auth: Authentication, name: String): Category?
}
