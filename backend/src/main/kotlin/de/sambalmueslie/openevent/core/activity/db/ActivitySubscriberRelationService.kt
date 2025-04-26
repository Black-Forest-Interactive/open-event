package de.sambalmueslie.openevent.core.activity.db


import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.activity.api.Activity
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime

@Singleton
class ActivitySubscriberRelationService(
    private val repository: ActivitySubscriberRelationRepository
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ActivitySubscriberRelationService::class.java)
    }

    fun addAccountInfoSubscriber(activity: Activity, subscriber: AccountInfo) {
        return addSubscriber(activity, setOf(subscriber.id))
    }

    fun addAccountInfoSubscriber(activity: Activity, subscribers: Collection<AccountInfo>) {
        return addSubscriber(activity, subscribers.map { it.id }.toSet())
    }

    fun addAccountSubscriber(activity: Activity, subscriber: Account) {
        return addSubscriber(activity, setOf(subscriber.id))
    }

    fun addAccountSubscriber(activity: Activity, subscribers: Collection<Account>) {
        return addSubscriber(activity, subscribers.map { it.id }.toSet())
    }

    private fun addSubscriber(activity: Activity, accountIds: Set<Long>) {
        val relations = accountIds.map { ActivitySubscriberRelation(activity.id, it, false, activity.timestamp) }
        repository.saveAll(relations)
    }

    fun getRecentForAccount(account: Account, pageable: Pageable): Page<ActivitySubscriberRelation> {
        return repository.findByAccountIdOrderByTimestamp(account.id, pageable)
    }

    fun getRecentForAccount(accountId: Long, pageable: Pageable): Page<ActivitySubscriberRelation> {
        return repository.findByAccountIdOrderByTimestamp(accountId, pageable)
    }

    fun markRead(account: Account, id: Long): ActivitySubscriberRelation? {
        val relation = repository.findByActivityIdAndAccountId(id, account.id) ?: return null
        relation.read = true
        repository.updateByActivityIdAndAccountId(id, account.id, true)
        return relation
    }

    fun markReadSingle(account: Account, id: Long) {
        repository.updateByActivityIdAndAccountId(id, account.id, true)
    }

    fun markReadAll(account: Account) {
        repository.updateByAccountId(account.id, true)
    }

    fun getUnreadForAccount(accountId: Long): List<ActivitySubscriberRelation> {
        return repository.findByAccountIdAndReadFalseOrderByTimestamp(accountId, Pageable.from(0, 20)).content
    }

    fun getUnreadForAccount(accountId: Long, timestamp: LocalDateTime, pageable: Pageable): Page<ActivitySubscriberRelation> {
        return repository.findByAccountIdAndReadFalseAndTimestampGreaterThanEqualsOrderByTimestamp(accountId, timestamp, pageable)
    }

    fun countUnreadForAccount(accountId: Long): Long {
        return repository.countByAccountIdAndReadFalse(accountId)
    }

    fun getByActivityIds(activityIds: Set<Long>): List<ActivitySubscriberRelation> {
        return repository.findByActivityIdIn(activityIds)
    }

    fun deleteByActivityId(activityIds: Set<Long>) {
        repository.deleteByActivityIdIn(activityIds)
    }
}
