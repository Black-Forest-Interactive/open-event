package de.sambalmueslie.openevent.core.activity

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.activity.api.ActivitySource
import de.sambalmueslie.openevent.core.activity.api.ActivitySourceChangeRequest

interface ActivitySourceStorage : Storage<Long, ActivitySource, ActivitySourceChangeRequest> {
    fun findByKey(key: String): ActivitySource?
    fun findByIds(ids: Set<Long>): List<ActivitySource>
    fun filterSubscriber(source: ActivitySource, accountIds: Set<Long>): Set<Long>
}