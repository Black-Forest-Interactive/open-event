package de.sambalmueslie.openevent.storage.notification


import de.sambalmueslie.openevent.core.model.NotificationScheme
import de.sambalmueslie.openevent.core.model.NotificationSchemeChangeRequest
import de.sambalmueslie.openevent.core.model.PatchRequest
import de.sambalmueslie.openevent.core.storage.NotificationSchemeStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.BaseStorageService
import de.sambalmueslie.openevent.storage.SimpleDataObjectConverter
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationSchemeStorageService(
    private val repository: NotificationSchemeRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, NotificationScheme, NotificationSchemeChangeRequest, NotificationSchemeData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    NotificationScheme::class,
    logger
), NotificationSchemeStorage {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationSchemeStorageService::class.java)
    }

    override fun createData(
        request: NotificationSchemeChangeRequest,
        properties: Map<String, Any>
    ): NotificationSchemeData {
        return NotificationSchemeData.create(request, timeProvider.now())
    }

    override fun updateData(
        data: NotificationSchemeData,
        request: NotificationSchemeChangeRequest
    ): NotificationSchemeData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: NotificationSchemeChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Name cannot be blank.")
    }

    override fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationScheme? {
        return patchData(id) { it.setEnabled(value.value, timeProvider.now()) }
    }

}
