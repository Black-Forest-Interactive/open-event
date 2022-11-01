package de.sambalmueslie.openevent.group.api

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.common.crud.BusinessObjectChangeRequest

@JsonIgnoreProperties(ignoreUnknown = true)
data class GroupChangeRequest(
    @JsonProperty("name")
    val name: String
) : BusinessObjectChangeRequest
