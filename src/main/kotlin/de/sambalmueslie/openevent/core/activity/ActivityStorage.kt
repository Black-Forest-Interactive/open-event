package de.sambalmueslie.openevent.core.activity

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityChangeRequest
import de.sambalmueslie.openevent.core.activity.api.ActivityInfo
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface ActivityStorage : Storage<Long, Activity, ActivityChangeRequest> {
    fun create(request: ActivityChangeRequest, actor: Account): Activity
    fun getRecentForAccount(account: Account, pageable: Pageable): Page<Activity>
    fun getRecentInfosForAccount(account: Account, pageable: Pageable): Page<ActivityInfo>
    fun markRead(account: Account, id: Long): Activity?
    fun getUnreadInfosForAccount(account: Account): List<ActivityInfo>
    fun markReadAll(account: Account): List<ActivityInfo>
}