package de.sambalmueslie.openevent.core.finder.db

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
data class FinderEntryId(
    @Column val finderId: Long,
    @Column val eventId: Long,
)
