package de.sambalmueslie.openevent.core.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class RegistrationInfo(
    val registration: Registration,
    val participants: List<Participant>,
)
