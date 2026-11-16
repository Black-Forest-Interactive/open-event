package de.sambalmueslie.openevent.core.finder.api

import java.time.LocalDateTime

data class FinderEntry(
    val finderId: Long,
    val eventId: Long,
    val answer: FinderAnswer,
    val timestamp: LocalDateTime,
)
