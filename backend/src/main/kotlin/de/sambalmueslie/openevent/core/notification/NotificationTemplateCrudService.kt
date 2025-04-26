package de.sambalmueslie.openevent.core.notification


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplate
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.notification.api.NotificationType
import de.sambalmueslie.openevent.core.notification.db.NotificationTemplateStorage
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
