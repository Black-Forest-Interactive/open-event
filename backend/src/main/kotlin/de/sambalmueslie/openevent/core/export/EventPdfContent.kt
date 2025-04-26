package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo

data class EventPdfContent(
    val event: Event,
    val location: Location,
    val registration: RegistrationInfo,
    val categories: List<Category>,
    val qrCode: String,
    val availableSpace: List<Char>
)
