package de.sambalmueslie.openevent.core.address.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest

data class AddressChangeRequest(
    val street: String,
    val streetNumber: String,
    val zip: String,
    val city: String,
    val country: String,
    val additionalInfo: String,

    val lat: Double,
    val lon: Double,
) : BusinessObjectChangeRequest
