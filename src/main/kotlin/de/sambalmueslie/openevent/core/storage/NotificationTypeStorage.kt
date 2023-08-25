package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.NotificationType
import de.sambalmueslie.openevent.core.model.NotificationTypeChangeRequest

interface NotificationTypeStorage : Storage<Long, NotificationType, NotificationTypeChangeRequest> {
    fun findByKey(key: String): NotificationType?
    fun findByKeys(keys: Set<String>): List<NotificationType>
}
