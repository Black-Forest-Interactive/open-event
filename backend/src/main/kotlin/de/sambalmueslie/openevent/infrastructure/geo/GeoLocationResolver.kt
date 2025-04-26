package de.sambalmueslie.openevent.infrastructure.geo

import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest

interface GeoLocationResolver {
    fun get(location: Location): GeoLocation?
    fun get(address: Address): GeoLocation?
    fun get(request: LocationChangeRequest): GeoLocation?
    fun get(request: AddressChangeRequest): GeoLocation?
}
