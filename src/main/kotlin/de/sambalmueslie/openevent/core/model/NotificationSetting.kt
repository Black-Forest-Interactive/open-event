package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject

data class NotificationSetting(
    override val id: Long,
    val name: String,
    val enabled: Boolean
) : BusinessObject<Long>

