package de.sambalmueslie.openevent.core.message.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class MessageChangeRequest(
    val subject: String,
    val content: String,
) : BusinessObjectChangeRequest
