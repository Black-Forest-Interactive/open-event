package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObject

data class NotificationScheme(
    override val id: Long,
    val name: String,
    val enabled: Boolean,
) : BusinessObject<Long>
