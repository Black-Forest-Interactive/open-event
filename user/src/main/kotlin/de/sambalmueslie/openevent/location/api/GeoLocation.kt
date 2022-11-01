package de.sambalmueslie.openevent.location.api

import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.common.crud.BusinessObject


data class GeoLocation(
    @JsonProperty("id")
    override val id: Long,
    @JsonProperty("lat")
    val lat: Double,
    @JsonProperty("lon")
    val lon: Double
): BusinessObject<Long>
