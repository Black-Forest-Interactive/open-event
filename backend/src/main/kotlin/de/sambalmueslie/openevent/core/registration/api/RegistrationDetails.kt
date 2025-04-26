package de.sambalmueslie.openevent.core.registration.api

import de.sambalmueslie.openevent.core.participant.api.ParticipantDetails

data class RegistrationDetails(
    val registration: Registration,
    val participants: List<ParticipantDetails>,
)
