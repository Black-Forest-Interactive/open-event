package de.sambalmueslie.openevent.core.event.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Suppress("JpaMissingIdInspection")
@Entity(name = "EventBookmark")
@Table(name = "event_bookmark")
data class EventBookmarkRelation(
    @Column val eventId: Long,
    @Column val accountId: Long,
)
