package de.sambalmueslie.openevent.core.activity

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PageSequence
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.api.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.util.concurrent.atomic.AtomicBoolean

@Singleton
class ActivityCrudService(
    private val storage: ActivityStorage,
) : BaseCrudService<Long, Activity, ActivityChangeRequest, ActivityChangeListener>(storage) {

    companion object {
        private val logger = LoggerFactory.getLogger(ActivityCrudService::class.java)
    }

    fun create(actor: Account, request: ActivityChangeRequest, source: ActivitySource, type: ActivityType): Activity {
        val result = storage.create(request, actor, source, type)
        notifyCreated(actor, result)
        return result
    }

    fun getForAccount(account: Account, id: Long): Activity? {
        val activity = get(id) ?: return null
        if (activity.actor.id == account.id) return activity
        return null
    }

    fun getRecentForAccount(account: Account, pageable: Pageable): Page<Activity> {
        return storage.getRecentForAccount(account, pageable)
    }

    fun getRecentInfosForAccount(account: Account, pageable: Pageable): Page<ActivityInfo> {
        return storage.getRecentInfosForAccount(account, pageable)
    }

    fun getUnreadInfosForAccount(account: Account): List<ActivityInfo> {
        return storage.getUnreadInfosForAccount(account)
    }


    fun getUnreadForAccount(account: Account, timestamp: LocalDateTime, pageable: Pageable): Page<Activity> {
        return storage.getUnreadForAccount(account, timestamp, pageable)
    }

    fun countUnreadForAccount(account: Account): Long {
        return storage.countUnreadForAccount(account)
    }

    fun markReadSingle(account: Account, id: Long) {
        storage.markReadSingle(account, id)
    }

    fun markReadAll(account: Account) {
        storage.markReadAll(account)
    }

    private val cleanupRunning = AtomicBoolean(false)

    fun cleanup(actor: Account, request: ActivityCleanupRequest) {
        if (cleanupRunning.get()) return
        cleanupRunning.set(true)
        try {
            val pages = PageSequence { storage.findBefore(request.timestamp, it) }
            pages.forEach { page ->
                val activities = page.content
                storage.deleteAll(activities)
                activities.forEach { notifyDeleted(actor, it) }
            }
        } catch (e: Exception) {
            logger.error("Failed while cleanup", e)
        }
        cleanupRunning.set(false)
    }
}