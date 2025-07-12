package de.sambalmueslie.openevent.core.event.api


import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest
import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest
import java.time.LocalDateTime

data class EventChangeRequest(
    val start: LocalDateTime,
    val finish: LocalDateTime,

    val title: String,
    val shortText: String,
    val longText: String,
    val imageUrl: String,
    val iconUrl: String,

    val categoryIds: Set<Long>,
    val location: LocationChangeRequest?,
    val registration: RegistrationChangeRequest,
    val published: Boolean,
    val shared: Boolean,
    val tags: Set<String>,
) : BusinessObjectChangeRequest
