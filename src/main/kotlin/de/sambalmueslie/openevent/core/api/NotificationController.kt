package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.NotificationAPI
import de.sambalmueslie.openevent.api.NotificationAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.NotificationAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.NotificationAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.logic.notification.NotificationSchemeCrudService
import de.sambalmueslie.openevent.core.logic.notification.NotificationSettingCrudService
import de.sambalmueslie.openevent.core.logic.notification.NotificationTemplateCrudService
import de.sambalmueslie.openevent.core.model.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/notification")
@Tag(name = "Notification API")
class NotificationController(
    private val schemeService: NotificationSchemeCrudService,
    private val templateService: NotificationTemplateCrudService,
    private val settingService: NotificationSettingCrudService,
) : NotificationAPI {

    @Get("/settings")
    override fun getSettings(auth: Authentication, pageable: Pageable): Page<NotificationSetting> {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { settingService.getAll(pageable) }
    }

    @Get("/setting/by/name/{name}")
    override fun findSettingByName(auth: Authentication, name: String): NotificationSetting? {
        return auth.checkPermission(PERMISSION_READ, PERMISSION_ADMIN) { settingService.findByName(name) }
    }

    @Put("/setting/{id}/enabled")
    override fun setSettingEnabled(auth: Authentication, id: Long, value: PatchRequest<Boolean>): NotificationSetting? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) { settingService.setEnabled(id, value) }
    }

    @Get("/scheme/{id}")
    override fun get(auth: Authentication, id: Long): NotificationScheme? {
        return auth.checkPermission(PERMISSION_READ) { schemeService.get(id) }
    }

    @Get("/scheme")
    override fun getAll(auth: Authentication, pageable: Pageable): Page<NotificationScheme> {
        return auth.checkPermission(PERMISSION_READ) { schemeService.getAll(pageable) }
    }

    @Post("/scheme")
    override fun create(auth: Authentication, @Body request: NotificationSchemeChangeRequest): NotificationScheme {
        return auth.checkPermission(PERMISSION_WRITE) { schemeService.create(request) }
    }
    @Put("/setting/{id}/enabled")
    override fun setSchemeEnabled(auth: Authentication, id: Long, value: PatchRequest<Boolean>): NotificationScheme? {
        return auth.checkPermission(PERMISSION_WRITE, PERMISSION_ADMIN) { schemeService.setEnabled(id, value) }
    }

    @Put("/scheme/{id}")
    override fun update(
        auth: Authentication,
        id: Long,
        @Body request: NotificationSchemeChangeRequest
    ): NotificationScheme {
        return auth.checkPermission(PERMISSION_WRITE) { schemeService.update(id, request) }
    }

    @Delete("/scheme/{id}")
    override fun delete(auth: Authentication, id: Long): NotificationScheme? {
        return auth.checkPermission(PERMISSION_WRITE) { schemeService.delete(id) }
    }

    @Post("/scheme/{schemeId}/template")
    override fun createTemplate(
        auth: Authentication,
        schemeId: Long,
        @Body request: NotificationTemplateChangeRequest
    ): NotificationTemplate? {
        return auth.checkPermission(PERMISSION_WRITE) { schemeService.createTemplate(schemeId, request) }
    }

    @Put("/template/{id}")
    override fun updateTemplate(
        auth: Authentication,
        id: Long,
        @Body request: NotificationTemplateChangeRequest
    ): NotificationTemplate {
        return auth.checkPermission(PERMISSION_WRITE) { templateService.update(id, request) }
    }

    @Delete("/template/{id}")
    override fun deleteTemplate(auth: Authentication, id: Long): NotificationTemplate? {
        return auth.checkPermission(PERMISSION_WRITE) { templateService.delete(id) }
    }

    @Get("/scheme/{schemeId}/template")
    override fun getTemplates(auth: Authentication, schemeId: Long, pageable: Pageable): Page<NotificationTemplate> {
        return auth.checkPermission(PERMISSION_READ) { schemeService.getTemplates(schemeId, pageable) }
    }


}
