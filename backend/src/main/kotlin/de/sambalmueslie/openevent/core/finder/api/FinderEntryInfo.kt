package de.sambalmueslie.openevent.core.finder.api

import de.sambalmueslie.openevent.core.search.api.EventSearchEntry
import java.time.LocalDateTime

data class FinderEntryInfo(
    val entry: EventSearchEntry,
    val answer: FinderAnswer,
    val timestamp: LocalDateTime,
)
