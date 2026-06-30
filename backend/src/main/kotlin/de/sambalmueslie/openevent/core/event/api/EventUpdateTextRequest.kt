package de.sambalmueslie.openevent.core.event.api

data class EventUpdateTextRequest(
    val title: String,
    val shortText: String,
    val longText: String,
)
