package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
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
