package de.sambalmueslie.openevent.core.share.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class SharedLocation(
    val street: String,
    val streetNumber: String,
    val zip: String,
    val city: String,
    val country: String,
    val additionalInfo: String,

    val lat: Double,
    val lon: Double,

    val size: Int
)
