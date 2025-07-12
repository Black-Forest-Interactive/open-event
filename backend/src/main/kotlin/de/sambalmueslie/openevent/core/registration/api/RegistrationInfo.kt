package de.sambalmueslie.openevent.core.registration.api

import de.sambalmueslie.openevent.core.participant.api.Participant

data class RegistrationInfo(
    val registration: Registration,
    val participants: List<Participant>,
)
