package de.sambalmueslie.openevent.core.notification.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Suppress("JpaMissingIdInspection")
@Entity(name = "SchemeSubscriberRelation")
@Table(name = "notification_scheme_subscriber_relation")
data class SchemeSubscriberRelation(
    @Column val schemeId: Long,
    @Column val accountId: Long,
)
