package de.sambalmueslie.openevent.core.logic.export

import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.Location
import de.sambalmueslie.openevent.core.model.RegistrationInfo

data class EventPdfContent(
    val event: Event,
    val location: Location,
    val registration: RegistrationInfo,
    val categories: List<Category>,
    val qrCode: String,
    val availableSpace: List<Char>
)
