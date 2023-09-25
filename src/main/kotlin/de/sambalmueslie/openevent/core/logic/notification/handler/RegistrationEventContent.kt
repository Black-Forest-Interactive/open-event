package de.sambalmueslie.openevent.core.logic.notification.handler

import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.Location
import de.sambalmueslie.openevent.core.model.Participant
import de.sambalmueslie.openevent.core.model.Registration

data class RegistrationEventContent(
    val event: Event,
    val registration: Registration,
    val participant: Participant,
    val location: Location
)
