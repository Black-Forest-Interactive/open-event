package de.sambalmueslie.openevent.core.announcement.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class AnnouncementChangeRequest(
    val subject: String,
    val content: String
) : BusinessObjectChangeRequest
