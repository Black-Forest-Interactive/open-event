package de.sambalmueslie.openevent.gateway.backoffice.settings

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.settings.api.SettingChangeRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/settings")
@Tag(name = "BACKOFFICE Settings API")
class SettingsController(private val service: SettingsService) {
    companion object {
        private const val PERMISSION_READ = "settings.read"
        private const val PERMISSION_WRITE = "settings.write"
        private const val PERMISSION_ADMIN = "settings.admin"
    }


    @Get("/{id}")
    fun get(auth: Authentication, @PathVariable id: Long) = auth.checkPermission(PERMISSION_ADMIN) {
        service.get(id)
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) {
        service.getAll(pageable)
    }

    @Post()
    fun create(auth: Authentication, @Body request: SettingChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            service.create(request)
        }

    @Put("/{id}")
    fun update(auth: Authentication, @PathVariable id: Long, @Body request: SettingChangeRequest) =
        auth.checkPermission(PERMISSION_ADMIN) {
            service.update(id, request)
        }

    @Delete("/{id}")
    fun delete(auth: Authentication, @PathVariable id: Long) = auth.checkPermission(PERMISSION_ADMIN) {
        service.delete(id)
    }
}