package de.sambalmueslie.openevent.infrastructure.settings


import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import de.sambalmueslie.openevent.api.SettingsAPI
import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.settings.api.Setting
import de.sambalmueslie.openevent.infrastructure.settings.api.SettingChangeRequest
import de.sambalmueslie.openevent.infrastructure.settings.db.SettingData
import de.sambalmueslie.openevent.infrastructure.settings.db.SettingsRepository
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.TimeUnit

@Singleton
class SettingsService(
    private val repository: SettingsRepository,
    private val timeProvider: TimeProvider,
    cacheService: CacheService,
) : BaseStorageService<Long, Setting, SettingChangeRequest, SettingData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Setting::class,
    logger
) {

    private val keyCache: LoadingCache<String, Setting?> = cacheService.register("Settings-Key") {
        Caffeine.newBuilder()
            .maximumSize(100)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .recordStats()
            .build { key -> repository.findByKey(key)?.convert() }
    }

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(SettingsService::class.java)
    }

    override fun getAll(pageable: Pageable): Page<Setting> {
        return repository.findAllOrderById(pageable).map { it.convert() }
    }

    override fun createData(request: SettingChangeRequest, properties: Map<String, Any>): SettingData {
        keyCache.invalidate(request.key)
        return SettingData.create(request, timeProvider.now())
    }

    override fun updateData(data: SettingData, request: SettingChangeRequest): SettingData {
        keyCache.invalidate(request.key)
        return data.update(request, timeProvider.now())
    }

    fun setValue(id: Long, value: Any): Setting? {
        val result = patchData(id) { it.setValue(value) } ?: return null
        keyCache.invalidate(result.key)
        return result
    }

    fun findByKey(key: String): Setting? {
        return keyCache[key]
    }

    fun getTitle(): String {
        return findByKey(SettingsAPI.SETTINGS_TEXT_TITLE)?.value as? String ?: ""
    }

    fun getTerms(): String {
        return findByKey(SettingsAPI.SETTINGS_TEXT_TERMS_AND_CONDITIONS)?.value as? String ?: ""
    }

    fun getLanguage(): String {
        return findByKey(SettingsAPI.SETTINGS_DEFAULT_LANGUAGE)?.value as? String ?: ""
    }

    fun getShareUrl(): String {
        return findByKey(SettingsAPI.SETTINGS_URL_SHARE)?.value as? String ?: ""
    }


    override fun getByIds(ids: Set<Long>): List<Setting> {
        return repository.findByIdIn(ids).map { it.convert() }
    }

}
