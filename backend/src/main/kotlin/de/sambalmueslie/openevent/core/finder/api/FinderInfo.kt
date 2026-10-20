package de.sambalmueslie.openevent.core.finder.api

import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import java.time.LocalDateTime

data class FinderInfo(
    val id: Long,
    val owner: AccountInfo,
    val request: EventSearchRequest,
    val name: String,
    val timestamp: LocalDateTime,
    val entries: List<FinderEntryInfo>
)
