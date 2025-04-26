package de.sambalmueslie.openevent.core.activity

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivityType
import de.sambalmueslie.openevent.core.activity.api.ActivityTypeChangeRequest

interface ActivityTypeStorage : Storage<Long, ActivityType, ActivityTypeChangeRequest> {
    fun create(request: ActivityTypeChangeRequest, source: ActivitySource): ActivityType
    fun findBySource(source: ActivitySource): List<ActivityType>
    fun findByKey(key: String): ActivityType?
    fun findByIds(ids: Set<Long>): List<ActivityType>
    fun filterSubscriber(type: ActivityType, accountIds: Set<Long>): Set<Long>
}