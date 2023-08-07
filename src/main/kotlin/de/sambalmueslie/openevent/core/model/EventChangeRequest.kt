package de.sambalmueslie.openevent.core.model


import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
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
    val registration: RegistrationChangeRequest
) : BusinessObjectChangeRequest
