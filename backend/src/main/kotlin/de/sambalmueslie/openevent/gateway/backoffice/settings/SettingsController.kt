package de.sambalmueslie.openevent.gateway.backoffice.settings

import de.sambalmueslie.openevent.infrastructure.settings.api.SettingChangeRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/settings")
@Tag(name = "BACKOFFICE Settings API")
class SettingsController(private val service: SettingsGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, @PathVariable id: Long) = service.get(auth, id)

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)

    @Post()
    fun create(auth: Authentication, @Body request: SettingChangeRequest) = service.create(auth, request)

    @Put("/{id}")
    fun update(auth: Authentication, @PathVariable id: Long, @Body request: SettingChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, @PathVariable id: Long) = service.delete(auth, id)

    @Get("title")
    fun getTitle(auth: Authentication) = service.getTitle(auth)
}
