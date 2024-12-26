package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.event.api.EventStats
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.registration.api.Registration
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface EventAPI : CrudAPI<Long, Event, EventChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.event.read"
        const val PERMISSION_WRITE = "openevent.event.write"
        const val PERMISSION_ADMIN = "openevent.event.admin"
        const val PERMISSION_MODERATOR = "openevent.event.mod"
    }

    fun setPublished(auth: Authentication, id: Long, value: PatchRequest<Boolean>): Event?
    fun getInfo(auth: Authentication, id: Long): EventInfo?
    fun getInfos(auth: Authentication, pageable: Pageable): Page<EventInfo>
    fun getLocation(auth: Authentication, id: Long): Location?
    fun getRegistration(auth: Authentication, id: Long): Registration?
    fun getCategories(auth: Authentication, id: Long): List<Category>

    fun getStats(auth: Authentication): List<EventStats>
    fun create(auth: Authentication, ownerId: Long, request: EventChangeRequest): Event
}
