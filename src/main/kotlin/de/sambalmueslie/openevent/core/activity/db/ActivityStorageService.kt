package de.sambalmueslie.openevent.core.activity.db

import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.ActivityStorage
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.api.ActivityInfo
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
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
    }

    override fun create(request: ActivityChangeRequest, actor: Account): Activity {
        return create(request, mapOf(Pair(ACTOR_REFERENCE, actor)))
    }

    override fun createData(request: ActivityChangeRequest, properties: Map<String, Any>): ActivityData {
        val actor = properties[ACTOR_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        unreadInfosCache.invalidateAll()
        return ActivityData.create(actor, request, timeProvider.now())
    }

    override fun updateData(data: ActivityData, request: ActivityChangeRequest): ActivityData {
        unreadInfosCache.invalidateAll()
        return data.update(request, timeProvider.now())
    }

    override fun deleteDependencies(data: ActivityData) {
        unreadInfosCache.invalidateAll()
    }

    override fun isValid(request: ActivityChangeRequest) {
        if (request.title.isBlank()) throw InvalidRequestException("Title cannot be blank")
        if (request.sourceId <= 0) throw InvalidRequestException("Source id must be valid")
    }

    override fun getRecentForAccount(account: Account, pageable: Pageable): Page<Activity> {
        val activities = repository.findRecentForAccount(account.id, pageable)
        return converter.convert(activities)
    }

    override fun getRecentInfosForAccount(account: Account, pageable: Pageable): Page<ActivityInfo> {
        val activities = converter.convert(repository.findRecentForAccount(account.id, pageable))
        val subscriptions = subscriberRelationService.getByActivityIds(activities.map { it.id }.toSet()).associateBy { it.activityId }
        return activities.map { ActivityInfo(it, subscriptions[it.id]?.read ?: true) }
    }

    override fun markRead(account: Account, id: Long): Activity? {
        val relation = subscriberRelationService.markRead(account, id) ?: return null
        unreadInfosCache.invalidate(relation.accountId)
        return get(id)
    }

    override fun markReadAll(account: Account): List<ActivityInfo> {
        subscriberRelationService.markReadAll(account)
        unreadInfosCache.invalidate(account.id)
        return getUnreadInfosForAccount(account)
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
        val page = subscriberRelationService.getUnreadInfosForAccount(accountId)
        val recentSubscriptions = page.associateBy { it.activityId }
        val activities = converter.convert(repository.findByIdIn(recentSubscriptions.keys))
        return activities.map { ActivityInfo(it, recentSubscriptions[it.id]?.read ?: true) }
    }


}