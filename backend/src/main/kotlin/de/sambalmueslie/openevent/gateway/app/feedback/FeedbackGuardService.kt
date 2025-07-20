package de.sambalmueslie.openevent.gateway.app.feedback

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.feedback.FeedbackCrudService
import de.sambalmueslie.openevent.core.feedback.api.FeedbackChangeRequest
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class FeedbackGuardService(
    private val service: FeedbackCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {

    companion object {
        private const val PERMISSION_READ = "feedback.read"
        private const val PERMISSION_WRITE = "feedback.write"
    }

    private val logger = audit.getLogger("APP Feedback API")

    fun create(auth: Authentication, @Body request: FeedbackChangeRequest, http: HttpRequest<*>): HttpResponse<Any> {
        auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: return@checkPermission
            service.create(account, account, request, http)
        }
        return HttpResponse.created("")
    }

}