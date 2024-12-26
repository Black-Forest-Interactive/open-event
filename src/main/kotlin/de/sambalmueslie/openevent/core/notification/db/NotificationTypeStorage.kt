package de.sambalmueslie.openevent.core.notification.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.notification.api.NotificationType
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest

interface NotificationTypeStorage : Storage<Long, NotificationType, NotificationTypeChangeRequest> {
    fun findByKey(key: String): NotificationType?
    fun findByKeys(keys: Set<String>): List<NotificationType>
}
