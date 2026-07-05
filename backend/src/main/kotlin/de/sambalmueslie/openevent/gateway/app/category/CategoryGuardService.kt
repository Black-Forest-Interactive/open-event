package de.sambalmueslie.openevent.gateway.app.category

import de.sambalmueslie.openevent.core.category.CategoryCrudService
import de.sambalmueslie.openevent.core.checkPermission
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class CategoryGuardService(private val service: CategoryCrudService) {
    companion object {
        private const val PERMISSION_READ = "category.read"
    }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_READ) { service.get(id) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
}
