package de.sambalmueslie.openevent.infrastructure.geo

import de.sambalmueslie.openevent.core.model.Location
import de.sambalmueslie.openevent.core.model.LocationChangeRequest

interface GeoLocationResolver {
    fun get(location: Location): GeoLocation?
    fun get(request: LocationChangeRequest): GeoLocation?
}
