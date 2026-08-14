package de.sambalmueslie.openevent.infrastructure.metrics

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.getExternalId
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditAction
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogger
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsProbe
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsSource
import io.micronaut.security.authentication.Authentication


internal class MetricsProbeImpl(
    private val writer: BufferedMetricsWriter,
    private val source: MetricsSource,
    private val type: String,
    private val logger: AuditLogger
) : MetricsProbe {

    companion object {
        const val ACTION = "access"
    }


    override fun <T> traceAccess(auth: Authentication, resource: Long, function: () -> T): T =
        traceAccess(auth.getExternalId(), resource, function)

    override fun <T> traceAccess(identity: String, resource: Long, function: () -> T): T {
        val result = function.invoke()
        writer.add(source, type, ACTION, identity, resource)
        return result
    }

    override fun info(actor: String, action: AuditAction, referenceId: String, reference: Any) {
        logger.info(actor, action, referenceId, reference)
    }

    override fun warning(actor: String, action: AuditAction, referenceId: String, reference: Any) {
        logger.warning(actor, action, referenceId, reference)
    }

    override fun error(actor: String, action: AuditAction, referenceId: String, reference: Any) {
        logger.error(actor, action, referenceId, reference)
    }

    override fun trace(actor: String, action: AuditAction, referenceId: String, reference: Any) {
        logger.trace(actor, action, referenceId, reference)
    }

    override fun <T : BusinessObject<*>, R : BusinessObjectChangeRequest> traceCreate(auth: Authentication, request: R, function: () -> T): T {
        return logger.traceCreate(auth, request, function)
    }

    override fun <T : BusinessObject<*>, R : BusinessObjectChangeRequest> traceUpdate(auth: Authentication, request: R, function: () -> T): T {
        return logger.traceUpdate(auth, request, function)
    }

    override fun <T : BusinessObject<*>> traceDelete(auth: Authentication, function: () -> T?): T? {
        return logger.traceDelete(auth, function)
    }

    override fun <T, R : Any> traceAction(auth: Authentication, action: AuditAction, referenceId: String, request: R, function: () -> T?): T? {
        return logger.traceAction(auth, action, referenceId, request, function)
    }

    override fun <T> traceAction(auth: Authentication, action: AuditAction, referenceId: String, function: () -> T?): T? {
        return logger.traceAction(auth, action, referenceId, function)
    }
}