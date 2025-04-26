package de.sambalmueslie.openevent.core.registration.api

import de.sambalmueslie.openevent.common.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Registration(
    override val id: Long,
    val eventId: Long,
    val maxGuestAmount: Int,
    val interestedAllowed: Boolean,
    val ticketsEnabled: Boolean
) : BusinessObject<Long>
