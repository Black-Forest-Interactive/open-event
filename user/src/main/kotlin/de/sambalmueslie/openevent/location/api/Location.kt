package de.sambalmueslie.openevent.location.api

import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.common.crud.BusinessObject

data class Location(
    @JsonProperty("id")
    override val id: Long,
    @JsonProperty("address")
    val address: Address,
    @JsonProperty("geoLocation")
    val geoLocation: GeoLocation,
    @JsonProperty("capacity")
    val capacity: Int
) : BusinessObject<Long>
