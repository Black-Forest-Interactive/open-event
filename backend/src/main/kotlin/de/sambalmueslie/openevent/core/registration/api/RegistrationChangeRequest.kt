package de.sambalmueslie.openevent.core.registration.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class RegistrationChangeRequest(
    val maxGuestAmount: Int,
    val interestedAllowed: Boolean,
    val ticketsEnabled: Boolean
) : BusinessObjectChangeRequest
