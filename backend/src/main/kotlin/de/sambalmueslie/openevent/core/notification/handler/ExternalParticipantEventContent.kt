package de.sambalmueslie.openevent.core.notification.handler

import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.participant.db.ExternalParticipantData
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.share.api.Share

data class ExternalParticipantEventContent(
    val share: Share,
    val event: Event,
    val registration: Registration,
    val participant: ExternalParticipantData
)
