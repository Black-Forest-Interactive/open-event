package de.sambalmueslie.openevent.group.db

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Suppress("JpaObjectClassSignatureInspection", "JpaMissingIdInspection")
@Entity(name = "GroupUserRelation")
@Table(name = "group_user_relation")
data class GroupUserRelation(
    @Column()
    var groupId: Long,
    @Column()
    var userId: Long
)
