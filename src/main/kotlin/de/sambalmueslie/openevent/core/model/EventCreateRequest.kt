package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest

data class EventCreateRequest(
    val account: Account,
    val content: EventChangeRequest
): BusinessObjectChangeRequest
