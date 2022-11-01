package de.sambalmueslie.openevent.group.api

import com.fasterxml.jackson.annotation.JsonProperty
import de.sambalmueslie.openevent.common.crud.BusinessObject

data class Group(
    @JsonProperty("id")
    override val id: Long,
    @JsonProperty("name")
    val name: String
) : BusinessObject<Long>
