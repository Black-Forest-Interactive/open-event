package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObject

data class NotificationType(
    override val id: Long,
    val key: String,
    val name: String,
    val description: String
) : BusinessObject<Long>
