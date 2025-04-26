package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.api.NotificationAPI
import de.sambalmueslie.openevent.api.NotificationAPI.Companion.PERMISSION_ADMIN
import de.sambalmueslie.openevent.api.NotificationAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.NotificationAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.CoreAPI
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.notification.api.*
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/notification")
@Tag(name = "Notification API")
@CoreAPI
class NotificationController(
    private val schemeService: NotificationSchemeCrudService,
    private val templateService: NotificationTemplateCrudService,
    private val settingService: NotificationSettingCrudService,
    private val typeService: NotificationTypeCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) : NotificationAPI {
    private val logger = audit.getLogger("Notification API")

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
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceCreate(auth, request) { schemeService.create(accountService.find(auth), request) }
        }
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
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceUpdate(auth, request) { schemeService.update(accountService.find(auth), id, request) }
        }
    }

    @Delete("/scheme/{id}")
    override fun delete(auth: Authentication, id: Long): NotificationScheme? {
        return auth.checkPermission(PERMISSION_WRITE) {
            logger.traceDelete(auth) { schemeService.delete(accountService.find(auth), id) }
        }
    }

    @Post("/type/{typeId}/template")
    override fun createTemplate(
        auth: Authentication,
        typeId: Long,
        @Body request: NotificationTemplateChangeRequest
    ): NotificationTemplate? {
        return auth.checkPermission(PERMISSION_WRITE) {
            typeService.createTemplate(accountService.find(auth), typeId, request)
        }
    }

    @Put("/template/{id}")
    override fun updateTemplate(
        auth: Authentication,
        id: Long,
        @Body request: NotificationTemplateChangeRequest
    ): NotificationTemplate {
        return auth.checkPermission(PERMISSION_WRITE) { templateService.update(accountService.find(auth), id, request) }
    }

    @Delete("/template/{id}")
    override fun deleteTemplate(auth: Authentication, id: Long): NotificationTemplate? {
        return auth.checkPermission(PERMISSION_WRITE) { templateService.delete(accountService.find(auth), id) }
    }

    @Get("/type/{typeId}/template")
    override fun getTemplates(auth: Authentication, typeId: Long, pageable: Pageable): Page<NotificationTemplate> {
        return auth.checkPermission(PERMISSION_READ) { typeService.getTemplates(typeId, pageable) }
    }

    @Get("/type")
    override fun getTypes(auth: Authentication, pageable: Pageable): Page<NotificationType> {
        return auth.checkPermission(PERMISSION_READ) { typeService.getAll(pageable) }
    }


}
