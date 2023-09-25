package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.HistoryEntry
import de.sambalmueslie.openevent.core.model.HistoryEventInfo
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface HistoryAPI : ReadAPI<Long, HistoryEntry> {
    companion object {
        const val PERMISSION_READ = "openevent.history.read"
        const val PERMISSION_ADMIN = "openevent.history.admin"
    }

    fun getForEvent(auth: Authentication, eventId: Long, pageable: Pageable): Page<HistoryEntry>
    fun getInfos(auth: Authentication, pageable: Pageable): Page<HistoryEventInfo>

}
