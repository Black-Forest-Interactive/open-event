package de.sambalmueslie.openevent.infrastructure.audit.db

import com.fasterxml.jackson.databind.ObjectMapper
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntry
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntryChangeRequest
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogLevel
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Suppress("JpaObjectClassSignatureInspection")
@Entity(name = "AuditLogEntry")
@Table(name = "audit_log_entry")
data class AuditLogEntryData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long,
    @Column var timestamp: LocalDateTime,
    @Column var actor: String,
    @Column @Enumerated(EnumType.STRING) var level: AuditLogLevel,
    @Column var message: String,
    @Column var request: String,
    @Column var referenceId: String,
    @Column var reference: String,
    @Column var source: String,
) : SimpleDataObject<AuditLogEntry> {

    companion object {
        fun create(request: AuditLogEntryChangeRequest, mapper: ObjectMapper): AuditLogEntryData {
            return AuditLogEntryData(
                0,
                request.timestamp,
                request.actor,
                request.level,
                request.message,
                mapper.writeValueAsString(request.request),
                request.referenceId,
                mapper.writeValueAsString(request.reference),
                request.source
            )
        }
    }

    override fun convert(): AuditLogEntry {
        return AuditLogEntry(id, timestamp, actor, level, message, request, referenceId, reference, source)
    }

    fun update(r: AuditLogEntryChangeRequest, mapper: ObjectMapper): AuditLogEntryData {
        timestamp = r.timestamp
        actor = r.actor
        level = r.level
        message = r.message
        request = mapper.writeValueAsString(r.request)
        referenceId = r.referenceId
        reference = mapper.writeValueAsString(r.reference)
        source = r.source
        return this
    }
}

