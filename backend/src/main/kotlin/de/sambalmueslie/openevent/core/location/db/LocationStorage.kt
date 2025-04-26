package de.sambalmueslie.openevent.core.location.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest

interface LocationStorage : Storage<Long, Location, LocationChangeRequest> {
    fun create(request: LocationChangeRequest, event: Event): Location
    fun findByEvent(event: Event): Location?
    fun findByEventIds(eventIds: Set<Long>): List<Location>
}
