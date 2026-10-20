package de.sambalmueslie.openevent.core.finder.db

import de.sambalmueslie.openevent.common.DataObject
import de.sambalmueslie.openevent.core.finder.api.FinderAnswer
import de.sambalmueslie.openevent.core.finder.api.FinderEntry
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "FinderEntry")
@Table(name = "finder_entry")
data class FinderEntryData(
    @EmbeddedId var id: FinderEntryId,
    @Column @Enumerated(EnumType.STRING) var answer: FinderAnswer,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : DataObject {
    companion object {
        fun create(id: FinderEntryId, answer: FinderAnswer, timestamp: LocalDateTime): FinderEntryData {
            return FinderEntryData(id, answer, timestamp)
        }
    }

    fun convert(): FinderEntry {
        return FinderEntry(id.finderId, id.eventId, answer, updated ?: created)
    }

    fun update(answer: FinderAnswer, now: LocalDateTime): FinderEntryData {
        this.answer = answer
        this.updated = now
        return this
    }
}
