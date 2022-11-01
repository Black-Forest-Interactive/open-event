package de.sambalmueslie.openevent.location.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.common.crud.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class LocationChangeRequest(
    @JsonProperty("address")
    val address: AddressChangeRequest,
    @JsonProperty("geoLocation")
    val geoLocation: GeoLocationChangeRequest,
    @JsonProperty("capacity")
    val capacity: Int
) : BusinessObjectChangeRequest
