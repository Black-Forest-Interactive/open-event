package de.sambalmueslie.openevent.storage.notification

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Suppress("JpaMissingIdInspection")
@Entity(name = "TypeSchemeRelation")
@Table(name = "notification_type_scheme_relation")
data class TypeSchemeRelation(
    @Column val typeId: Long,
    @Column val schemeId: Long
)
