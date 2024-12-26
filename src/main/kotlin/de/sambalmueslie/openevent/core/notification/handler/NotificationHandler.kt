package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest

interface NotificationHandler {
    fun getName(): String
    fun getTypes(): Set<NotificationTypeChangeRequest>
}
