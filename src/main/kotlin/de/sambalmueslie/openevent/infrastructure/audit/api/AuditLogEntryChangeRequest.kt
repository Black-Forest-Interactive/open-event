package de.sambalmueslie.openevent.infrastructure.audit.api

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import java.time.LocalDateTime

data class AuditLogEntryChangeRequest(
    val timestamp: LocalDateTime,
    val actor: String,
    val level: AuditLogLevel,
    val message: String,
    val request: Any,
    val referenceId: String,
    val reference: Any,
    val source: String,
) : BusinessObjectChangeRequest
