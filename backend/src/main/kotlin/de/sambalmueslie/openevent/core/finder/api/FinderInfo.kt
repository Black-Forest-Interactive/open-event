package de.sambalmueslie.openevent.core.finder.api

import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import java.time.LocalDateTime

data class FinderInfo(
    val id: Long,
    val name: String,
    val request: EventSearchRequest,
    val timestamp: LocalDateTime,
    val countOpen: Long,
    val countYes: Long,
    val countNo: Long,
    val countMaybe: Long,
)
