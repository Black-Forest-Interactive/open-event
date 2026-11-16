package de.sambalmueslie.openevent.core.finder.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import java.time.LocalDateTime

data class Finder(
    override val id: Long,
    val ownerId: Long,
    val name: String,
    val request: EventSearchRequest,
    val timestamp: LocalDateTime
) : BusinessObject<Long>
