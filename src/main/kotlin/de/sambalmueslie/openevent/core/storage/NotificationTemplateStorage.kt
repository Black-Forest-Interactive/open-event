package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.NotificationTemplate
import de.sambalmueslie.openevent.core.model.NotificationTemplateChangeRequest
import de.sambalmueslie.openevent.core.model.NotificationType
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import java.util.*

interface NotificationTemplateStorage : Storage<Long, NotificationTemplate, NotificationTemplateChangeRequest> {

    fun create(type: NotificationType, request: NotificationTemplateChangeRequest): NotificationTemplate
    fun findByType(type: NotificationType, pageable: Pageable): Page<NotificationTemplate>
    fun findByTypeAndLang(type: NotificationType, lang: Locale): NotificationTemplate?
}
