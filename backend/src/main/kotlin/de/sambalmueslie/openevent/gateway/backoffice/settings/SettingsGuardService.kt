package de.sambalmueslie.openevent.gateway.backoffice.settings

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.settings.api.SettingChangeRequest
import de.sambalmueslie.openevent.infrastructure.settings.api.TextResponse
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class SettingsGuardService(private val service: SettingsService) {
    companion object {
        private const val PERMISSION_ADMIN = "settings.admin"
    }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun create(auth: Authentication, request: SettingChangeRequest) = auth.checkPermission(PERMISSION_ADMIN) { service.create(request) }

    fun update(auth: Authentication, id: Long, request: SettingChangeRequest) = auth.checkPermission(PERMISSION_ADMIN) { service.update(id, request) }

    fun delete(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.delete(id) }

    fun getTitle(auth: Authentication) = auth.checkPermission(PERMISSION_ADMIN) { TextResponse(service.getTitle()) }
}
