package de.sambalmueslie.openevent.core.model

data class HistoryEventInfo(
    val event: Event,
    val entries: List<HistoryEntry>
)
