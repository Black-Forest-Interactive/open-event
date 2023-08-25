package de.sambalmueslie.openevent.infrastructure.audit.api

import de.sambalmueslie.openevent.core.BusinessObject
import java.time.LocalDateTime

data class AuditLogEntry(
    override val id: Long,
    val timestamp: LocalDateTime,
    val user: String,
    val level: AuditLogLevel,
    val message: String,
    val request: String,
    val referenceId: String,
    val reference: String,
    val source: String,
) : BusinessObject<Long>
