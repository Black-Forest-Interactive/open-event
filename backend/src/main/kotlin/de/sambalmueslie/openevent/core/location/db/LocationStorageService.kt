package de.sambalmueslie.openevent.core.location.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class LocationStorageService(
    private val repository: LocationRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Location, LocationChangeRequest, LocationData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Location::class,
    logger
), LocationStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LocationStorageService::class.java)
        private const val EVENT_REFERENCE = "event"
    }

    override fun create(request: LocationChangeRequest, event: Event): Location {
        return create(request, mapOf(Pair(EVENT_REFERENCE, event)))
    }

    override fun createData(request: LocationChangeRequest, properties: Map<String, Any>): LocationData {
        val event = properties[EVENT_REFERENCE] as? Event ?: throw InvalidRequestException("Cannot find event")
        return LocationData.create(event, request, timeProvider.now())
    }

    override fun updateData(data: LocationData, request: LocationChangeRequest): LocationData {
        return data.update(request, timeProvider.now())
    }


    override fun findByEvent(event: Event): Location? {
        return repository.findByEventId(event.id)?.convert()
    }

    override fun findByEventIds(eventIds: Set<Long>): List<Location> {
        return repository.findByEventIdIn(eventIds).map { it.convert() }
    }

}
