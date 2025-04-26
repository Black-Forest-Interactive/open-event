package de.sambalmueslie.openevent.core.activity.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Suppress("JpaMissingIdInspection")
@Entity(name = "ActivitySourceSubscriberRelation")
@Table(name = "activity_source_subscriber")
data class ActivitySourceSubscriberRelation(
    @Column val accountId: Long,
    @Column val sourceId: Long,
)
