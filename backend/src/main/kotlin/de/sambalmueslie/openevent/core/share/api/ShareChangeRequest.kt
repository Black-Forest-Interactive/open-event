package de.sambalmueslie.openevent.core.share.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class ShareChangeRequest(
    val eventId: Long,
    val published: Boolean
) : BusinessObjectChangeRequest