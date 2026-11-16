package de.sambalmueslie.openevent.core.history.api

import de.sambalmueslie.openevent.core.search.api.EventSearchEntry

data class HistoryEventInfo(
    val event: EventSearchEntry,
    val entries: List<HistoryEntry>
)
