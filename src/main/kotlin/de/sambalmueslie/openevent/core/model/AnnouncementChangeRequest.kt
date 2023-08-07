package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AnnouncementChangeRequest(
    val subject: String,
    val content: String
) : BusinessObjectChangeRequest
