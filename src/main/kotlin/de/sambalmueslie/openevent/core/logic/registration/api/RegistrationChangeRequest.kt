package de.sambalmueslie.openevent.core.logic.registration.api

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class RegistrationChangeRequest(
    val maxGuestAmount: Int,
    val interestedAllowed: Boolean,
    val ticketsEnabled: Boolean
) : BusinessObjectChangeRequest
