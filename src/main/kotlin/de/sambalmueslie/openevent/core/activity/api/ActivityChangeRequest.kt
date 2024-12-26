package de.sambalmueslie.openevent.core.activity.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.account.api.Account

data class ActivityChangeRequest(
    val title: String,
    val actor: Account,
    val source: ActivitySource,
    val type: ActivityType,
    val sourceId: Long
) : BusinessObjectChangeRequest
