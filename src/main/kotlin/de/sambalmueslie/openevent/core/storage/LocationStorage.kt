package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.Location
import de.sambalmueslie.openevent.core.model.LocationChangeRequest

interface LocationStorage : Storage<Long, Location, LocationChangeRequest> {
    fun create(request: LocationChangeRequest, event: Event): Location
    fun findByEvent(event: Event): Location?
    fun findByEventIds(eventIds: Set<Long>): List<Location>
}
