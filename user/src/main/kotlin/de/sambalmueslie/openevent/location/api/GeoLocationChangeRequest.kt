package de.sambalmueslie.openevent.location.api


import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.common.crud.BusinessObjectChangeRequest
@JsonIgnoreProperties(ignoreUnknown = true)
data class GeoLocationChangeRequest(
    @JsonProperty("lat")
    val lat: Double,
    @JsonProperty("lon")
    val lon: Double
) : BusinessObjectChangeRequest
