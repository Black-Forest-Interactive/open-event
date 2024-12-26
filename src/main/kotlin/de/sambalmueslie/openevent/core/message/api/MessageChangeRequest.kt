package de.sambalmueslie.openevent.core.message.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class MessageChangeRequest(
    val subject: String,
    val content: String,
) : BusinessObjectChangeRequest
