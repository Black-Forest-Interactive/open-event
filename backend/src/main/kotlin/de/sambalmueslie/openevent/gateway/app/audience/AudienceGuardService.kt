package de.sambalmueslie.openevent.gateway.app.audience

import de.sambalmueslie.openevent.core.audience.AudienceCrudService
import de.sambalmueslie.openevent.core.checkPermission
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class AudienceGuardService(private val service: AudienceCrudService) {
    companion object {
        private const val PERMISSION_READ = "audience.read"
    }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_READ) { service.get(id) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
}
