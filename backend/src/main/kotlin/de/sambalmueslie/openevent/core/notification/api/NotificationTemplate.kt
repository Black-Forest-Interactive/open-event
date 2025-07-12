package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObject

data class NotificationTemplate(
    override val id: Long,
    val subject: String,
    val lang: String,
    val content: String,
    val plain: Boolean,
) : BusinessObject<Long>
