package de.sambalmueslie.openevent.core.logic.location


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.Location
import de.sambalmueslie.openevent.core.model.LocationChangeRequest
import de.sambalmueslie.openevent.core.storage.LocationStorage
import de.sambalmueslie.openevent.infrastructure.geo.GeoLocationResolver
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class LocationCrudService(
    private val storage: LocationStorage,
    private val geoLocationResolver: GeoLocationResolver
) : BaseCrudService<Long, Location, LocationChangeRequest>(storage, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LocationCrudService::class.java)
    }

    fun create(event: Event, request: LocationChangeRequest): Location {
        val result = storage.create(resolveGeoLocation(request), event)
        notifyCreated(result)
        return result
    }

    fun findByEvent(event: Event): Location? {
        return storage.findByEvent(event)
    }

    fun updateByEvent(event: Event, request: LocationChangeRequest): Location {
        val existing = storage.findByEvent(event) ?: return create(event, request)

        val result = storage.update(existing.id, resolveGeoLocation(request))
        notifyUpdated(result)
        return result
    }

    private fun resolveGeoLocation(request: LocationChangeRequest): LocationChangeRequest {
        val geoLocation = geoLocationResolver.get(request) ?: return request

        return LocationChangeRequest(
            request.street,
            request.streetNumber,
            request.zip,
            request.city,
            request.country,
            request.additionalInfo,
            geoLocation.lat,
            geoLocation.lon,
            request.size
        )
    }

    fun deleteByEvent(event: Event): Location? {
        val existing = storage.findByEvent(event) ?: return null
        storage.delete(existing.id)
        notifyDeleted(existing)
        return existing
    }

    fun findByEventIds(eventIds: Set<Long>): List<Location> {
        return storage.findByEventIds(eventIds)
    }


}
