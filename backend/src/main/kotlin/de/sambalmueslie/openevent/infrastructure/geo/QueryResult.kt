package de.sambalmueslie.openevent.infrastructure.geo

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import io.micronaut.serde.annotation.Serdeable

@JsonIgnoreProperties(ignoreUnknown = true)
@Serdeable
data class QueryResult(
    @JsonProperty("lat")
    val lat: String,
    @JsonProperty("lon")
    val lon: String
)
