package de.sambalmueslie.openevent.core.event.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Suppress("JpaMissingIdInspection")
@Entity(name = "EventAudience")
@Table(name = "event_audience")
data class EventAudienceRelation(
    @Column val eventId: Long,
    @Column val audienceId: Long,
)
