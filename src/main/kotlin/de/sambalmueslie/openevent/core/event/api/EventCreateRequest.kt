package de.sambalmueslie.openevent.core.event.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.account.api.Account

data class EventCreateRequest(
    val account: Account,
    val content: EventChangeRequest
) : BusinessObjectChangeRequest
