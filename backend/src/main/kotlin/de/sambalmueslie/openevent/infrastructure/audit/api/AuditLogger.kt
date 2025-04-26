package de.sambalmueslie.openevent.infrastructure.audit.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.security.authentication.Authentication

interface AuditLogger {

    fun info(actor: String, message: String, referenceId: String, reference: Any)
    fun warning(actor: String, message: String, referenceId: String, reference: Any)
    fun error(actor: String, message: String, referenceId: String, reference: Any)
    fun trace(actor: String, message: String, referenceId: String, reference: Any)

    fun <T : BusinessObject<*>, R : BusinessObjectChangeRequest> traceCreate(
        auth: Authentication,
        request: R,
        function: () -> T
    ): T

    fun <T : BusinessObject<*>, R : BusinessObjectChangeRequest> traceUpdate(
        auth: Authentication,
        request: R,
        function: () -> T
    ): T

    fun <T : BusinessObject<*>> traceDelete(
        auth: Authentication,
        function: () -> T?
    ): T?

    fun <T, R : Any> traceAction(
        auth: Authentication,
        message: String,
        referenceId: String,
        request: R,
        function: () -> T?
    ): T?

    fun <T> traceAction(
        auth: Authentication,
        message: String,
        referenceId: String,
        function: () -> T?
    ): T?
}
