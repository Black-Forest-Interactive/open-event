package de.sambalmueslie.openevent.core.location.api

import de.sambalmueslie.openevent.common.BusinessObject

data class Location(
    override val id: Long,

    val eventId: Long,

    val street: String,
    val streetNumber: String,
    val zip: String,
    val city: String,
    val country: String,
    val additionalInfo: String,

    val lat: Double,
    val lon: Double,

    val size: Int

) : BusinessObject<Long> {
    fun format(): String {
        return "$street $streetNumber, $zip $city"
    }
}
