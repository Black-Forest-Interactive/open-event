package de.sambalmueslie.openevent.infrastructure.audit


import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.getEmail
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

    override fun info(actor: String, message: String, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.INFO, message, referenceId, reference, this.source)
    }

    override fun warning(actor: String, message: String, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.WARNING, message, referenceId, reference, this.source)
    }

    override fun error(actor: String, message: String, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.ERROR, message, referenceId, reference, this.source)
    }

    override fun trace(actor: String, message: String, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.TRACE, message, referenceId, reference, this.source)
    }

    fun trace(actor: String, message: String, request: Any, referenceId: String, reference: Any) {
        log(actor, AuditLogLevel.TRACE, message, referenceId, reference, this.source, request)
    }

    override fun <T : BusinessObject<*>, R : BusinessObjectChangeRequest> traceCreate(
        auth: Authentication,
        request: R,
        function: () -> T
    ): T {
        val result = function.invoke()
        trace(auth.getEmail(), "CREATE", request, result.id.toString(), result)
        return result
    }

    override fun <T : BusinessObject<*>, R : BusinessObjectChangeRequest> traceUpdate(
        auth: Authentication,
        request: R,
        function: () -> T
    ): T {
        val result = function.invoke()
        trace(auth.getEmail(), "UPDATE", request, result.id.toString(), result)
        return result
    }

    override fun <T : BusinessObject<*>> traceDelete(auth: Authentication, function: () -> T?): T? {
        val result = function.invoke() ?: return null
        trace(auth.getEmail(), "DELETE", result.id.toString(), result)
        return result
    }

    override fun <T> traceAction(auth: Authentication, message: String, referenceId: String, function: () -> T?): T? {
        val result = function.invoke() ?: return null
        trace(auth.getEmail(), message, referenceId, result)
        return result
    }

    override fun <T, R : Any> traceAction(
        auth: Authentication,
        message: String,
        referenceId: String,
        request: R,
        function: () -> T?
    ): T? {
        val result = function.invoke() ?: return null
        trace(auth.getEmail(), message, request, referenceId, result)
        return result
    }

    private fun log(
        actor: String,
        level: AuditLogLevel,
        message: String,
        referenceId: String,
        reference: Any,
        source: String,
        request: Any = "",
    ) {
        service.create(
            AuditLogEntryChangeRequest(
                timeProvider.now(),
                actor, level, message, request, referenceId, reference, source
            )
        )
    }


}
