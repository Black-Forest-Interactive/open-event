package de.sambalmueslie.openevent.storage.event

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Suppress("JpaMissingIdInspection")
@Entity(name = "EventAnnouncement")
@Table(name = "event_annoncement")
data class EventAnnouncementRelation(
    @Column val eventId: Long,
    @Column val announcementId: Long,
)
