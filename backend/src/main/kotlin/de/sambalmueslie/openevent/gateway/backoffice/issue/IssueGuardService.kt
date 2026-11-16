package de.sambalmueslie.openevent.gateway.backoffice.issue

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.issue.IssueCrudService
import de.sambalmueslie.openevent.core.issue.api.Issue
import de.sambalmueslie.openevent.core.issue.api.IssueStatus
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class IssueGuardService(
    private val service: IssueCrudService,
    private val accountService: AccountCrudService,
) {

    companion object {
        private const val PERMISSION_ADMIN = "issue.admin"
    }

    fun get(auth: Authentication, id: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.get(id) }

    fun getAll(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getAll(pageable) }

    fun getByAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Issue> =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
            service.getByAccount(account, pageable)
        }

    fun getUnresolvedByAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Issue> =
        auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
            service.getUnresolvedByAccount(account, pageable)
        }

    fun getUnresolved(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getUnresolved(pageable) }

    fun changeStatus(auth: Authentication, id: Long, status: PatchRequest<IssueStatus>) =
        auth.checkPermission(PERMISSION_ADMIN) {
            val actor = accountService.get(auth) ?: return@checkPermission null
            service.changeStatus(actor, id, status)
        }
}
