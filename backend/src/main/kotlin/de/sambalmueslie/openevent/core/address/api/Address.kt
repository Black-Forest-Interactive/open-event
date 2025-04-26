package de.sambalmueslie.openevent.core.address.api

import de.sambalmueslie.openevent.common.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Address(
    override val id: Long,

    val street: String,
    val streetNumber: String,
    val zip: String,
    val city: String,
    val country: String,
    val additionalInfo: String,

    val lat: Double,
    val lon: Double,
) : BusinessObject<Long> {
    fun format(): String {
        return "$street $streetNumber, $zip $city"
    }
}
