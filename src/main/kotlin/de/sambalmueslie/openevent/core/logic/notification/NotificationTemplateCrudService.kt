package de.sambalmueslie.openevent.core.logic.notification


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.NotificationTemplate
import de.sambalmueslie.openevent.core.model.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.model.NotificationType
import de.sambalmueslie.openevent.core.storage.NotificationTemplateStorage
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

@Singleton
class NotificationTemplateCrudService(
    private val storage: NotificationTemplateStorage
) : BaseCrudService<Long, NotificationTemplate, NotificationTemplateChangeRequest, NotificationTemplateChangeListener>(
    storage
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationTemplateCrudService::class.java)
    }

    fun create(
        actor: Account,
        type: NotificationType,
        request: NotificationTemplateChangeRequest
    ): NotificationTemplate {
        val result = storage.create(type, request)
        notifyCreated(actor, result)
        return result
    }

    fun findByType(type: NotificationType, pageable: Pageable): Page<NotificationTemplate> {
        return storage.findByType(type, pageable)
    }

    fun find(type: NotificationType, lang: Locale): NotificationTemplate? {
        return storage.findByTypeAndLang(type, lang)
    }


}
