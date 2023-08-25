package de.sambalmueslie.openevent.storage.notification


import de.sambalmueslie.openevent.core.model.NotificationType
import de.sambalmueslie.openevent.core.model.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.core.storage.NotificationTypeStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.BaseStorageService
import de.sambalmueslie.openevent.storage.SimpleDataObjectConverter
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationTypeStorageService(
    private val repository: NotificationTypeRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, NotificationType, NotificationTypeChangeRequest, NotificationTypeData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    NotificationType::class,
    logger
), NotificationTypeStorage {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationTypeStorageService::class.java)
    }

    override fun createData(
        request: NotificationTypeChangeRequest,
        properties: Map<String, Any>
    ): NotificationTypeData {
        return NotificationTypeData.create(request, timeProvider.now())
    }

    override fun updateData(
        data: NotificationTypeData,
        request: NotificationTypeChangeRequest
    ): NotificationTypeData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: NotificationTypeChangeRequest) {
        if (request.key.isBlank()) throw InvalidRequestException("Key cannot be blank.")
    }

    override fun findByKey(key: String): NotificationType? {
        return repository.findByKey(key)?.convert()
    }

    override fun findByKeys(keys: Set<String>): List<NotificationType> {
        return repository.findByKeyIn(keys).map { it.convert() }
    }

}
