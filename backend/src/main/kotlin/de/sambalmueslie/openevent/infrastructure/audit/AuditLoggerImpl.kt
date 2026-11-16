package de.sambalmueslie.openevent.infrastructure.audit


import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.getEmail
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditAction
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogEntryChangeRequest
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogLevel
import de.sambalmueslie.openevent.infrastructure.audit.api.AuditLogger
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.security.authentication.Authentication
import org.slf4j.Logger
import org.slf4j.LoggerFactory


internal class AuditLoggerImpl(
    private val service: AuditService,
    private val timeProvider: TimeProvider,
    private val source: String
) : AuditLogger {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AuditLoggerImpl::class.java)
    }

    override fun info(actor: String, action: AuditAction, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.INFO, action, referenceId, reference, this.source)
    }

    override fun warning(actor: String, action: AuditAction, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.WARNING, action, referenceId, reference, this.source)
    }

    override fun error(actor: String, action: AuditAction, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.ERROR, action, referenceId, reference, this.source)
    }

    override fun trace(actor: String, action: AuditAction, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.TRACE, action, referenceId, reference, this.source)
    }

    fun trace(actor: String, action: AuditAction, request: Any, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.TRACE, action, referenceId, reference, this.source, request)
    }

    override fun <T : BusinessObject<*>, R : BusinessObjectChangeRequest> traceCreate(
        auth: Authentication,
        request: R,
        function: () -> T
    ): T {
        val result = function.invoke()
        trace(auth.getEmail(), AuditAction.CREATE, request, result.id.toString(), result)
        return result
    }

    override fun <T : BusinessObject<*>, R : BusinessObjectChangeRequest> traceUpdate(
        auth: Authentication,
        request: R,
        function: () -> T
    ): T {
        val result = function.invoke()
        trace(auth.getEmail(), AuditAction.UPDATE, request, result.id.toString(), result)
        return result
    }

    override fun <T : BusinessObject<*>> traceDelete(auth: Authentication, function: () -> T?): T? {
        val result = function.invoke() ?: return null
        trace(auth.getEmail(), AuditAction.DELETE, result.id.toString(), result)
        return result
    }

    override fun <T> traceAction(auth: Authentication, action: AuditAction, referenceId: String, function: () -> T?): T? {
        val result = function.invoke() ?: return null
        trace(auth.getEmail(), action, referenceId, result)
        return result
    }

    override fun <T, R : Any> traceAction(auth: Authentication, action: AuditAction, referenceId: String, request: R, function: () -> T?): T? {
        val result = function.invoke() ?: return null
        trace(auth.getEmail(), action, request, referenceId, result)
        return result
    }

    private fun log(actor: String, level: AuditLogLevel, action: AuditAction, referenceId: String, reference: Any, source: String, request: Any = "") {
        service.create(
            AuditLogEntryChangeRequest(timeProvider.now(), actor, level, action, request, referenceId, reference, source)
        )
    }


}
