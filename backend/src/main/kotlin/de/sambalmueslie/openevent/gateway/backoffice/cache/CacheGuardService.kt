package de.sambalmueslie.openevent.gateway.backoffice.cache

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class CacheGuardService(private val service: CacheService) {
    companion object {
        private const val PERMISSION_ADMIN = "cache.admin"
    }

    fun get(auth: Authentication, key: String) = auth.checkPermission(PERMISSION_ADMIN) { service.get(key) }

    fun getAll(auth: Authentication) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll() }

    fun reset(auth: Authentication, key: String) = auth.checkPermission(PERMISSION_ADMIN) { service.reset(key) }

    fun resetAll(auth: Authentication) = auth.checkPermission(PERMISSION_ADMIN) { service.resetAll() }
}
