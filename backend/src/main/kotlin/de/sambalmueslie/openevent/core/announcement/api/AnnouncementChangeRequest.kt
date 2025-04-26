package de.sambalmueslie.openevent.core.announcement.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AnnouncementChangeRequest(
    val subject: String,
    val content: String
) : BusinessObjectChangeRequest
