package de.sambalmueslie.openevent.core.activity.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Suppress("JpaMissingIdInspection")
@Entity(name = "ActivityTypeSubscriberRelation")
@Table(name = "activity_type_subscriber")
data class ActivityTypeSubscriberRelation(
    @Column val accountId: Long,
    @Column val typeId: Long,
)
