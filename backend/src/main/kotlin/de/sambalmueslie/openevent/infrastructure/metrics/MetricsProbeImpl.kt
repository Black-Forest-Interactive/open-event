package de.sambalmueslie.openevent.infrastructure.metrics

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.getExternalId
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogger
import de.sambalmueslie.openevent.infrastructure.metrics.api.MetricsProbe
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.security.authentication.Authentication


internal class MetricsProbeImpl(
    private val service: MetricsService,
    private val timeProvider: TimeProvider,
    private val source: String,
    private val logger: AuditLogger
) : MetricsProbe {


    override fun <T> traceAccess(auth: Authentication, resource: Long, function: () -> T): T {
        val result = function.invoke()
        service.addMetricsEntry(source, "access", auth.getExternalId(), resource, timeProvider.now())
        return result
    }

    override fun info(actor: String, message: String, referenceId: String, reference: Any) {
        logger.info(actor, message, referenceId, reference)
    }

    override fun warning(actor: String, message: String, referenceId: String, reference: Any) {
        logger.warning(actor, message, referenceId, reference)
    }

    override fun error(actor: String, message: String, referenceId: String, reference: Any) {
        logger.error(actor, message, referenceId, reference)
    }

    override fun trace(actor: String, message: String, referenceId: String, reference: Any) {
        logger.trace(actor, message, referenceId, reference)
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

    override fun <T, R : Any> traceAction(auth: Authentication, message: String, referenceId: String, request: R, function: () -> T?): T? {
        return logger.traceAction(auth, message, referenceId, request, function)
    }

    override fun <T> traceAction(auth: Authentication, message: String, referenceId: String, function: () -> T?): T? {
        return logger.traceAction(auth, message, referenceId, function)
    }
}