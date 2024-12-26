package de.sambalmueslie.openevent.core.history.api

import de.sambalmueslie.openevent.core.event.api.Event

data class HistoryEventInfo(
    val event: Event,
    val entries: List<HistoryEntry>
)
