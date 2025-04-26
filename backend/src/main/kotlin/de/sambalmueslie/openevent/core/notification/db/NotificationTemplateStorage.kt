package de.sambalmueslie.openevent.core.notification.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplate
import de.sambalmueslie.openevent.core.notification.api.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.notification.api.NotificationType
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import java.util.*

interface NotificationTemplateStorage : Storage<Long, NotificationTemplate, NotificationTemplateChangeRequest> {

    fun create(type: NotificationType, request: NotificationTemplateChangeRequest): NotificationTemplate
    fun findByType(type: NotificationType, pageable: Pageable): Page<NotificationTemplate>
    fun findByTypeAndLang(type: NotificationType, lang: Locale): NotificationTemplate?
}
