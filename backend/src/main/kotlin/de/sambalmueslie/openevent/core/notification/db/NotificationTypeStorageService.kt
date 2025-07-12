package de.sambalmueslie.openevent.core.notification.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.notification.api.NotificationType
import de.sambalmueslie.openevent.core.notification.api.NotificationTypeChangeRequest
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
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

    override fun findByKey(key: String): NotificationType? {
        return repository.findByKey(key)?.convert()
    }

    override fun findByKeys(keys: Set<String>): List<NotificationType> {
        return repository.findByKeyIn(keys).map { it.convert() }
    }

}
