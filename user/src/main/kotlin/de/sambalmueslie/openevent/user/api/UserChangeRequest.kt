package de.sambalmueslie.openevent.user.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.common.crud.BusinessObjectChangeRequest
@JsonIgnoreProperties(ignoreUnknown = true)
data class UserChangeRequest(
    @JsonProperty("externalId")
    val externalId: String,
    @JsonProperty("type")
    val type: UserType,
    @JsonProperty("userName")
    val userName: String,
    @JsonProperty("firstName")
    val firstName: String,
    @JsonProperty("lastName")
    val lastName: String,
    @JsonProperty("email")
    val email: String,
    @JsonProperty("mobile")
    val mobile: String,
    @JsonProperty("phone")
    val phone: String,
) : BusinessObjectChangeRequest
