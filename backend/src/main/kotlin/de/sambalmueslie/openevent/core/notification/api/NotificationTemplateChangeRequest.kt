package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class NotificationTemplateChangeRequest(
    val subject: String,
    val lang: String,
    val content: String,
    val plain: Boolean,
) : BusinessObjectChangeRequest
