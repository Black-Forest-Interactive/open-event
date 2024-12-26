package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.registration.api.Registration

data class RegistrationEventContent(
    val event: Event,
    val registration: Registration,
    val participant: Participant,
    val location: Location
)
