package de.sambalmueslie.openevent.gateway.backoffice.search

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.search.SearchService
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class SearchGuardService(private val service: SearchService) {
    companion object {
        private const val PERMISSION_ADMIN = "search.admin"
    }

    fun getInfo(auth: Authentication) = auth.checkPermission(PERMISSION_ADMIN) { service.getInfo() }

    fun getInfo(auth: Authentication, key: String) = auth.checkPermission(PERMISSION_ADMIN) { service.getInfo(key) }

    fun setup(auth: Authentication, key: String) = auth.checkPermission(PERMISSION_ADMIN) { service.setup(key) }
}
