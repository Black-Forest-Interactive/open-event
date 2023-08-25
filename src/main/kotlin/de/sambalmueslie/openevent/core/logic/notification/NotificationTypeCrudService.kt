package de.sambalmueslie.openevent.core.logic.notification


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.*
import de.sambalmueslie.openevent.core.storage.NotificationTypeStorage
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationTypeCrudService(
    private val storage: NotificationTypeStorage,
    private val templateService: NotificationTemplateCrudService,
) : BaseCrudService<Long, NotificationType, NotificationTypeChangeRequest, NotificationTypeChangeListener>(storage) {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationTypeCrudService::class.java)
    }


    fun createTemplate(
        actor: Account,
        typeId: Long,
        request: NotificationTemplateChangeRequest
    ): NotificationTemplate? {
        val type = get(typeId) ?: return null
        return templateService.create(actor, type, request)
    }

    fun getTemplates(typeId: Long, pageable: Pageable): Page<NotificationTemplate> {
        val type = get(typeId) ?: return Page.empty()
        return templateService.findByType(type, pageable)
    }

    fun findByKey(key: String): NotificationType? {
        return storage.findByKey(key)
    }
}
