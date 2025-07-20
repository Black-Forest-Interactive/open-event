package de.sambalmueslie.openevent.infrastructure.metrics.api

import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogger
import io.micronaut.security.authentication.Authentication

interface MetricsProbe : AuditLogger {
    fun <T> traceAccess(auth: Authentication, resource: Long, function: () -> T): T
}