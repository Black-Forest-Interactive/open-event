package de.sambalmueslie.openevent.location.api

import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.common.crud.BusinessObject

data class Address(
    @JsonProperty("id")
    override val id: Long,
    @JsonProperty("street")
    val street: String,
    @JsonProperty("streetNumber")
    val streetNumber: String,
    @JsonProperty("zip")
    val zip: String,
    @JsonProperty("city")
    val city: String,
    @JsonProperty("country")
    val country: String,
    @JsonProperty("additionalInfo")
    val additionalInfo: String
) : BusinessObject<Long>
