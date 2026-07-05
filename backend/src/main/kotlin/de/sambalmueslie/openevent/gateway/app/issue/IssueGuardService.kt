package de.sambalmueslie.openevent.gateway.app.issue

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.issue.IssueCrudService
import de.sambalmueslie.openevent.core.issue.api.IssueChangeRequest
import de.sambalmueslie.openevent.infrastructure.audit.AuditService
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class IssueGuardService(
    private val service: IssueCrudService,
    private val accountService: AccountCrudService,
    audit: AuditService,
) {
    companion object {
        private const val PERMISSION_READ = "issue.read"
        private const val PERMISSION_WRITE = "issue.write"
    }

    private val logger = audit.getLogger("APP Issue API")

    fun create(auth: Authentication, request: IssueChangeRequest, http: HttpRequest<*>): HttpResponse<Any> {
        auth.checkPermission(PERMISSION_WRITE) {
            val account = accountService.get(auth) ?: return@checkPermission
            service.create(account, account, request, http)
        }
        return HttpResponse.created("")
    }
}
