package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntry
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntryChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface AuditAPI : CrudAPI<Long, AuditLogEntry, AuditLogEntryChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.audit.read"
        const val PERMISSION_WRITE = "openevent.audit.write"
    }


    fun findByReferenceId(auth: Authentication, referenceId: String, pageable: Pageable): Page<AuditLogEntry>
}
