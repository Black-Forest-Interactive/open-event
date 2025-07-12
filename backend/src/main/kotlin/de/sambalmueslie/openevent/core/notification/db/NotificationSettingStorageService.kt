package de.sambalmueslie.openevent.core.notification.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.notification.api.NotificationSetting
import de.sambalmueslie.openevent.core.notification.api.NotificationSettingChangeRequest
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class NotificationSettingStorageService(
    private val repository: NotificationSettingRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, NotificationSetting, NotificationSettingChangeRequest, NotificationSettingData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    NotificationSetting::class,
    logger
), NotificationSettingStorage {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(NotificationSettingStorageService::class.java)
    }

    override fun createData(
        request: NotificationSettingChangeRequest,
        properties: Map<String, Any>
    ): NotificationSettingData {
        return NotificationSettingData.create(request, timeProvider.now())
    }

    override fun updateData(
        data: NotificationSettingData,
        request: NotificationSettingChangeRequest
    ): NotificationSettingData {
        return data.update(request, timeProvider.now())
    }

    override fun findByName(name: String): NotificationSetting? {
        return repository.findByName(name)?.convert()
    }

    override fun setEnabled(id: Long, value: PatchRequest<Boolean>): NotificationSetting? {
        return patchData(id) { it.setEnabled(value.value, timeProvider.now()) }
    }

}
