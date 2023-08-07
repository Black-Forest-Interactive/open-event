package de.sambalmueslie.openevent.core.logic.notification


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.NotificationSchemeStorage
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationSchemeCrudService(
    private val storage: NotificationSchemeStorage,
    private val templateService: NotificationTemplateCrudService,
) : BaseCrudService<Long, NotificationScheme, NotificationSchemeChangeRequest>(storage, logger) {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationSchemeCrudService::class.java)
    }

    fun createTemplate(schemeId: Long, request: NotificationTemplateChangeRequest): NotificationTemplate? {
        val scheme = get(schemeId) ?: return null
        return templateService.create(scheme, request)
    }

    fun getTemplates(schemeId: Long, pageable: Pageable): Page<NotificationTemplate> {
        val scheme = get(schemeId) ?: return Page.empty()
        return templateService.findByScheme(scheme, pageable)
    }

    fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationScheme? {
        return storage.setEnabled(id, value)
    }

}
