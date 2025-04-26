package de.sambalmueslie.openevent.core.activity.db

import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.activity.ActivitySourceStorage
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivitySourceChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ActivitySourceStorageService(
    private val repository: ActivitySourceRepository,
    private val subscriberRepository: ActivitySourceSubscriberRelationRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, ActivitySource, ActivitySourceChangeRequest, ActivitySourceData>(
    repository, SimpleDataObjectConverter(), cacheService, ActivitySource::class, logger
), ActivitySourceStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ActivitySourceStorageService::class.java)
    }

    override fun createData(request: ActivitySourceChangeRequest, properties: Map<String, Any>): ActivitySourceData {
        return ActivitySourceData.create(request, timeProvider.now())
    }

    override fun updateData(data: ActivitySourceData, request: ActivitySourceChangeRequest): ActivitySourceData {
        return data.update(request, timeProvider.now())
    }

    @Deprecated("Move that to core")
    override fun isValid(request: ActivitySourceChangeRequest) {
        if (request.key.isBlank()) throw InvalidRequestException("Key cannot be blank")
    }

    override fun findByKey(key: String): ActivitySource? {
        return repository.findByKey(key)?.convert()
    }

    override fun filterSubscriber(source: ActivitySource, accountIds: Set<Long>): Set<Long> {
        return subscriberRepository.findBySourceIdAndAccountIdIn(source.id, accountIds).map { it.accountId }.toSet()
    }

    override fun findByIds(ids: Set<Long>): List<ActivitySource> {
        return repository.findByIdIn(ids).map { it.convert() }
    }
}