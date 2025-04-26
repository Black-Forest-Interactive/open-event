package de.sambalmueslie.openevent.core.activity

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.api.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import java.time.LocalDateTime

interface ActivityStorage : Storage<Long, Activity, ActivityChangeRequest> {
    fun create(request: ActivityChangeRequest, actor: Account, source: ActivitySource, type: ActivityType): Activity
    fun getRecentForAccount(account: Account, pageable: Pageable): Page<Activity>
    fun getRecentInfosForAccount(account: Account, pageable: Pageable): Page<ActivityInfo>
    fun markRead(account: Account, id: Long): Activity?
    fun getUnreadInfosForAccount(account: Account): List<ActivityInfo>
    fun getUnreadForAccount(account: Account, timestamp: LocalDateTime, pageable: Pageable): Page<Activity>
    fun countUnreadForAccount(account: Account): Long

    fun markReadSingle(account: Account, id: Long)
    fun markReadAll(account: Account)

    fun findBefore(timestamp: LocalDateTime, pageable: Pageable): Page<Activity>
    fun deleteAll(activities: List<Activity>)


}