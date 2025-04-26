package de.sambalmueslie.openevent.core.share.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class SharedRegistration(
    val maxGuestAmount: Int,
    val interestedAllowed: Boolean,
    val ticketsEnabled: Boolean,

    val participants: List<SharedParticipant>,
)
