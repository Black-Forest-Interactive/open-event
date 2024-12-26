package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplate
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.notification.api.NotificationType
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.core.notification.db.NotificationTypeStorage
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

    fun findByKeys(keys: Set<String>): List<NotificationType> {
        return storage.findByKeys(keys)
    }
}
