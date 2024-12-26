package de.sambalmueslie.openevent.core.history.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.history.api.HistoryEntry
import de.sambalmueslie.openevent.core.history.api.HistoryEntryChangeRequest
import de.sambalmueslie.openevent.core.history.api.HistoryEntrySource
import de.sambalmueslie.openevent.core.history.api.HistoryEntryType
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "HistoryEntry")
@Table(name = "history_entry")
data class HistoryEntryData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,

    @Column var eventId: Long,
    @Column var actorId: Long,

    @Column var timestamp: LocalDateTime,
    @Column @Enumerated(EnumType.STRING) var type: HistoryEntryType,
    @Column var message: String,
    @Column @Enumerated(EnumType.STRING) var source: HistoryEntrySource,
    @Column var info: String
) : DataObject {

    companion object {
        fun create(
            event: Event,
            actor: Account,
            request: HistoryEntryChangeRequest,
            timestamp: LocalDateTime
        ): HistoryEntryData {
            return HistoryEntryData(
                0,
                event.id,
                actor.id,

                timestamp,
                request.type,
                request.message,
                request.source,
                request.info
            )
        }
    }

    fun update(request: HistoryEntryChangeRequest, timestamp: LocalDateTime): HistoryEntryData {
        this.timestamp = timestamp
        type = request.type
        message = request.message
        source = request.source
        info = request.info
        return this
    }

    fun convert(actor: Account): HistoryEntry {
        return HistoryEntry(id, eventId, timestamp, actor, type, message, source, info)
    }

}
