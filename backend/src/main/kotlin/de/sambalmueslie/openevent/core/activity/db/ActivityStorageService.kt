package de.sambalmueslie.openevent.core.activity.db

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.ActivityStorage
import de.sambalmueslie.openevent.core.activity.api.*
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.concurrent.TimeUnit

@Singleton
class ActivityStorageService(
    private val repository: ActivityRepository,
    private val converter: ActivityConverter,

    private val subscriberRelationService: ActivitySubscriberRelationService,

    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Activity, ActivityChangeRequest, ActivityData>(
    repository,
    converter,
    cacheService,
    Activity::class,
    logger
), ActivityStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ActivityStorageService::class.java)
        private const val ACTOR_REFERENCE = "actor"
        private const val SOURCE_REFERENCE = "source"
        private const val TYPE_REFERENCE = "type"
    }

    override fun create(request: ActivityChangeRequest, actor: Account, source: ActivitySource, type: ActivityType): Activity {
        return create(request, mapOf(Pair(ACTOR_REFERENCE, actor), Pair(SOURCE_REFERENCE, source), Pair(TYPE_REFERENCE, type)))
    }

    override fun createData(request: ActivityChangeRequest, properties: Map<String, Any>): ActivityData {
        val actor = properties[ACTOR_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        val source = properties[SOURCE_REFERENCE] as? ActivitySource ?: throw InvalidRequestException("Cannot find source")
        val type = properties[TYPE_REFERENCE] as? ActivityType ?: throw InvalidRequestException("Cannot find type")
        unreadInfosCache.invalidateAll()
        return ActivityData.create(actor, request, source, type, timeProvider.now())
    }

    override fun updateData(data: ActivityData, request: ActivityChangeRequest): ActivityData {
        unreadInfosCache.invalidateAll()
        return data.update(request, timeProvider.now())
    }

    override fun deleteDependencies(data: ActivityData) {
        subscriberRelationService.deleteByActivityId(setOf(data.id))
        unreadInfosCache.invalidateAll()
    }

    @Deprecated("Move that to core")
    override fun isValid(request: ActivityChangeRequest) {
        if (request.title.isBlank()) throw InvalidRequestException("Title cannot be blank")
        if (request.referenceId <= 0) throw InvalidRequestException("Source id must be valid")
    }

    override fun getRecentForAccount(account: Account, pageable: Pageable): Page<Activity> {
        val activities = repository.findRecentForAccount(account.id, pageable)
        return converter.convert(activities)
    }

    override fun getRecentInfosForAccount(account: Account, pageable: Pageable): Page<ActivityInfo> {
        val activities = converter.convert(repository.findRecentForAccount(account.id, pageable))
        val subscriptions =
            subscriberRelationService.getByActivityIds(activities.map { it.id }.toSet()).associateBy { it.activityId }
        return activities.map { ActivityInfo(it, subscriptions[it.id]?.read ?: true) }
    }

    override fun markRead(account: Account, id: Long): Activity? {
        val relation = subscriberRelationService.markRead(account, id) ?: return null
        unreadInfosCache.invalidate(relation.accountId)
        return get(id)
    }

    override fun markReadSingle(account: Account, id: Long) {
        subscriberRelationService.markReadSingle(account, id)
        unreadInfosCache.invalidate(account.id)
    }

    override fun markReadAll(account: Account) {
        subscriberRelationService.markReadAll(account)
        unreadInfosCache.invalidate(account.id)
    }

    override fun getUnreadInfosForAccount(account: Account): List<ActivityInfo> {
        return unreadInfosCache[account.id]
    }

    private val unreadInfosCache: LoadingCache<Long, List<ActivityInfo>> =
        cacheService.register(ActivityInfo::class.java.simpleName) {
            Caffeine.newBuilder()
                .maximumSize(1000)
                .expireAfterWrite(1, TimeUnit.HOURS)
                .recordStats()
                .build { accountId -> collectUnreadInfosForAccount(accountId) }
        }

    private fun collectUnreadInfosForAccount(accountId: Long): List<ActivityInfo> {
        val page = subscriberRelationService.getUnreadForAccount(accountId)
        val recentSubscriptions = page.associateBy { it.activityId }
        val activities = converter.convert(repository.findByIdIn(recentSubscriptions.keys))
        return activities.map { ActivityInfo(it, recentSubscriptions[it.id]?.read ?: true) }
    }

    override fun getUnreadForAccount(account: Account, timestamp: LocalDateTime, pageable: Pageable): Page<Activity> {
        val content = subscriberRelationService.getUnreadForAccount(account.id, timestamp, pageable)
        val activityIds = content.map { it.activityId }.toSet()
        val activities = converter.convert(repository.findByIdIn(activityIds))
        return Page.of(activities, content.pageable, content.totalSize)
    }

    override fun countUnreadForAccount(account: Account): Long {
        return subscriberRelationService.countUnreadForAccount(account.id)
    }


    override fun findBefore(timestamp: LocalDateTime, pageable: Pageable): Page<Activity> {
        return converter.convert(repository.findByCreatedLessThan(timestamp, pageable))
    }

    override fun deleteAll(activities: List<Activity>) {
        val activityIds = activities.map { it.id }.toSet()
        subscriberRelationService.deleteByActivityId(activityIds)
        repository.deleteByIdIn(activityIds)
        unreadInfosCache.invalidateAll()
    }

}