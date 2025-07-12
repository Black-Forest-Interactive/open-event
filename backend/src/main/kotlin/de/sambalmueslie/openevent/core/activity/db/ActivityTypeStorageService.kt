package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.activity.ActivityTypeStorage
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import de.sambalmueslie.openevent.core.activity.api.ActivityTypeChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ActivityTypeStorageService(
    private val repository: ActivityTypeRepository,
    private val subscriberRepository: ActivityTypeSubscriberRelationRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, ActivityType, ActivityTypeChangeRequest, ActivityTypeData>(
    repository, SimpleDataObjectConverter(), cacheService, ActivityType::class, logger
), ActivityTypeStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ActivityTypeStorageService::class.java)
        private const val SOURCE_REFERENCE = "source"
    }

    override fun create(request: ActivityTypeChangeRequest, source: ActivitySource): ActivityType {
        return create(request, mapOf(Pair(SOURCE_REFERENCE, source)))
    }

    override fun createData(request: ActivityTypeChangeRequest, properties: Map<String, Any>): ActivityTypeData {
        val source = properties[SOURCE_REFERENCE] as? ActivitySource ?: throw InvalidRequestException("Cannot find source")
        return ActivityTypeData.create(request, source, timeProvider.now())
    }

    override fun updateData(data: ActivityTypeData, request: ActivityTypeChangeRequest): ActivityTypeData {
        return data.update(request, timeProvider.now())
    }

    override fun findBySource(source: ActivitySource): List<ActivityType> {
        return repository.findBySourceId(source.id).map { it.convert() }
    }

    override fun findByIds(ids: Set<Long>): List<ActivityType> {
        return repository.findByIdIn(ids).map { it.convert() }
    }

    override fun findByKey(key: String): ActivityType? {
        return repository.findByKey(key)?.convert()
    }

    override fun filterSubscriber(type: ActivityType, accountIds: Set<Long>): Set<Long> {
        return subscriberRepository.findByTypeIdAndAccountIdIn(type.id, accountIds).map { it.accountId }.toSet()
    }
}