package de.sambalmueslie.openevent.core.history.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.Account
import java.time.LocalDateTime

data class HistoryEntry(
    override val id: Long,
    val eventId: Long,
    val timestamp: LocalDateTime,
    val actor: Account,
    val type: HistoryEntryType,
    val message: String,
    val source: HistoryEntrySource,
    val info: String
) : BusinessObject<Long>
