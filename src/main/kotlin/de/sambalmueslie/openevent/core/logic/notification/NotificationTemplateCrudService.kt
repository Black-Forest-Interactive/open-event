package de.sambalmueslie.openevent.core.logic.notification


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.NotificationScheme
import de.sambalmueslie.openevent.core.model.NotificationTemplate
import de.sambalmueslie.openevent.core.model.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.storage.NotificationTemplateStorage
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationTemplateCrudService(
    private val storage: NotificationTemplateStorage
) : BaseCrudService<Long, NotificationTemplate, NotificationTemplateChangeRequest>(storage, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationTemplateCrudService::class.java)
    }

    fun create(scheme: NotificationScheme, request: NotificationTemplateChangeRequest): NotificationTemplate {
        val result = storage.create(scheme, request)
        notifyCreated(result)
        return result
    }

    fun findByScheme(scheme: NotificationScheme, pageable: Pageable): Page<NotificationTemplate> {
        return storage.findByScheme(scheme, pageable)
    }


}
