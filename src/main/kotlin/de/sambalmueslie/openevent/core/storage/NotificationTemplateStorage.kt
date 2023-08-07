package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.NotificationScheme
import de.sambalmueslie.openevent.core.model.NotificationTemplate
import de.sambalmueslie.openevent.core.model.NotificationTemplateChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface NotificationTemplateStorage : Storage<Long, NotificationTemplate, NotificationTemplateChangeRequest> {

    fun create(scheme: NotificationScheme, request: NotificationTemplateChangeRequest): NotificationTemplate
    fun findByScheme(scheme: NotificationScheme, pageable: Pageable): Page<NotificationTemplate>
}
