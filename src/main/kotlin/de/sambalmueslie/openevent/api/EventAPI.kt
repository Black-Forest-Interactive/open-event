package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.*
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface EventAPI : CrudAPI<Long, Event, EventChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.event.read"
        const val PERMISSION_WRITE = "openevent.event.write"
        const val PERMISSION_ADMIN = "openevent.event.admin"
    }

    fun setPublished(auth: Authentication, id: Long, value: PatchRequest<Boolean>): Event?
    fun getInfo(auth: Authentication, id: Long): EventInfo?
    fun getInfos(auth: Authentication, pageable: Pageable): Page<EventInfo>
    fun getLocation(auth: Authentication, id: Long): Location?
    fun getRegistration(auth: Authentication, id: Long): Registration?
    fun getCategories(auth: Authentication, id: Long): List<Category>
    fun search(auth: Authentication, query: String, pageable: Pageable): Page<EventInfo>
}
