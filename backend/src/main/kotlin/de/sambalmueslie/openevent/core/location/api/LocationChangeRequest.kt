package de.sambalmueslie.openevent.core.location.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class LocationChangeRequest(
    val street: String,
    val streetNumber: String,
    val zip: String,
    val city: String,
    val country: String,
    val additionalInfo: String,

    val lat: Double,
    val lon: Double,

    val size: Int
) : BusinessObjectChangeRequest
