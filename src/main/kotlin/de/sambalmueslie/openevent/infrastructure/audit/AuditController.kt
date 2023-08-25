package de.sambalmueslie.openevent.infrastructure.audit


import de.sambalmueslie.openevent.api.AuditAPI
import de.sambalmueslie.openevent.api.AuditAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.AuditAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntry
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntryChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/audit")
@Tag(name = "Audit API")
class AuditController(private val service: AuditService) : AuditAPI {


    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): AuditLogEntry? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get("/find/by/reference/{referenceId}")
    override fun findByReferenceId(auth: Authentication, referenceId: String, pageable: Pageable): Page<AuditLogEntry> {
        return auth.checkPermission(PERMISSION_READ) { service.findByReferenceId(referenceId, pageable) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<AuditLogEntry> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: AuditLogEntryChangeRequest): AuditLogEntry {
        return auth.checkPermission(PERMISSION_WRITE) { service.create(request) }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: AuditLogEntryChangeRequest): AuditLogEntry {
        return auth.checkPermission(PERMISSION_WRITE) { service.update(id, request) }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): AuditLogEntry? {
        return auth.checkPermission(PERMISSION_WRITE) { service.delete(id) }
    }
}
