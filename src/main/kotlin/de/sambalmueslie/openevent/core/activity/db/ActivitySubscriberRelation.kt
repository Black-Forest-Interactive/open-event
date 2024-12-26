package de.sambalmueslie.openevent.core.activity.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.LocalDateTime

@Suppress("JpaMissingIdInspection")
@Entity(name = "ActivitySubscriberRelation")
@Table(name = "activity_subscriber")
data class ActivitySubscriberRelation(
    @Column val activityId: Long,
    @Column val accountId: Long,
    @Column var read: Boolean,
    @Column val timestamp: LocalDateTime
)
