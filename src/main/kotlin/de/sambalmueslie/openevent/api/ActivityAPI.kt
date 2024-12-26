package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.activity.api.Activity
import de.sambalmueslie.openevent.core.activity.api.ActivityInfo
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface ActivityAPI : ReadAPI<Long, Activity> {
    companion object {
        const val PERMISSION_USER = "openevent.activity.user"
        const val PERMISSION_ADMIN = "openevent.activity.admin"
    }

    fun getRecentInfos(auth: Authentication, pageable: Pageable): Page<ActivityInfo>
    fun getUnreadInfos(auth: Authentication): List<ActivityInfo>
    fun markRead(auth: Authentication, id: Long): Activity?
    fun markReadAll(auth: Authentication): List<ActivityInfo>
}