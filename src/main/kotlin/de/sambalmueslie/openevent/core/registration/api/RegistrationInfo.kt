package de.sambalmueslie.openevent.core.registration.api

import de.sambalmueslie.openevent.core.participant.api.Participant
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class RegistrationInfo(
    val registration: Registration,
    val participants: List<Participant>,
)
