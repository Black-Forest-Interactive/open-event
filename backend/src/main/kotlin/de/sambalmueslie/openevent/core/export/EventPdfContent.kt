package de.sambalmueslie.openevent.core.export

import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import java.time.format.TextStyle
import java.util.*

data class EventPdfContent(
    val event: Event,
    val location: Location,
    val registration: RegistrationInfo,
    val categories: List<Category>,
    val audiences: List<Audience>,
    val qrCode: String,
    val availableSpace: List<Char>,
    val description: String,
    val shortDescription: String
) {
    val weekday: String = event.start.dayOfWeek.getDisplayName(TextStyle.FULL, Locale.GERMAN)
}
