package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface NotificationAPI : CrudAPI<Long, NotificationScheme, NotificationSchemeChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.notification.read"
        const val PERMISSION_WRITE = "openevent.notification.write"
        const val PERMISSION_ADMIN = "openevent.notification.admin"
    }

    fun setSchemeEnabled(auth: Authentication, id: Long, value: PatchRequest<Boolean>): NotificationScheme?
    fun getSettings(auth: Authentication, pageable: Pageable): Page<NotificationSetting>
    fun findSettingByName(auth: Authentication, name: String): NotificationSetting?
    fun setSettingEnabled(auth: Authentication, id: Long, value: PatchRequest<Boolean>): NotificationSetting?

    fun createTemplate(
        auth: Authentication,
        typeId: Long,
        request: NotificationTemplateChangeRequest
    ): NotificationTemplate?

    fun updateTemplate(
        auth: Authentication,
        id: Long,
        request: NotificationTemplateChangeRequest
    ): NotificationTemplate?

    fun deleteTemplate(auth: Authentication, id: Long): NotificationTemplate?

    fun getTemplates(auth: Authentication, typeId: Long, pageable: Pageable): Page<NotificationTemplate>

    fun getTypes(auth: Authentication, pageable: Pageable): Page<NotificationType>


}
