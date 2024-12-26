package de.sambalmueslie.openevent.core.registration.db


import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

@Singleton
class RegistrationStorageService(
    private val repository: RegistrationRepository,
    private val cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Registration, RegistrationChangeRequest, RegistrationData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Registration::class,
    logger
), RegistrationStorage {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(RegistrationStorageService::class.java)
        private const val EVENT_REFERENCE = "event"
    }

    override fun create(request: RegistrationChangeRequest, event: Event): Registration {
        return create(request, mapOf(Pair(EVENT_REFERENCE, event)))
    }

    override fun createData(request: RegistrationChangeRequest, properties: Map<String, Any>): RegistrationData {
        val event = properties[EVENT_REFERENCE] as? Event ?: throw InvalidRequestException("Cannot find event")
        return RegistrationData.create(event, request, timeProvider.now())
    }

    override fun updateData(data: RegistrationData, request: RegistrationChangeRequest): RegistrationData {
        cacheByEvent.invalidate(data.eventId)
        return data.update(request, timeProvider.now())
    }

    override fun deleteDependencies(data: RegistrationData) {
        cacheByEvent.invalidate(data.eventId)
    }

    override fun isValid(request: RegistrationChangeRequest) {
        if (request.maxGuestAmount <= 0) throw InvalidRequestException("Max guest must be positive number")
    }


    private val cacheByEvent: LoadingCache<Long, Registration> = cacheService.register("RegistrationByEvent") {
        Caffeine.newBuilder()
            .maximumSize(1000)
            .expireAfterWrite(12, TimeUnit.HOURS)
            .recordStats()
            .build { id -> repository.findByEventId(id)?.convert() }
    }

    override fun findByEvent(event: Event): Registration? {
        return cacheByEvent[event.id]
    }

    override fun findByEventIds(eventIds: Set<Long>): List<Registration> {
        return eventIds.mapNotNull { cacheByEvent[it] }
    }

}
