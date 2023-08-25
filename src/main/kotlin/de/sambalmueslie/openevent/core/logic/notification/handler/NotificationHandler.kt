package de.sambalmueslie.openevent.core.logic.notification.handler

import de.sambalmueslie.openevent.core.model.NotificationTypeChangeRequest

interface NotificationHandler {
    fun getName(): String
    fun getTypes(): Set<NotificationTypeChangeRequest>
}
