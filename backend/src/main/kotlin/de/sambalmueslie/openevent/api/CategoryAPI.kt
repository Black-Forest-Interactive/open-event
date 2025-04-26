package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import io.micronaut.security.authentication.Authentication

interface CategoryAPI : CrudAPI<Long, Category, CategoryChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.category.read"
        const val PERMISSION_WRITE = "openevent.category.write"
        const val PERMISSION_ADMIN = "openevent.category.admin"
    }

    fun findByName(auth: Authentication, name: String): Category?
}
