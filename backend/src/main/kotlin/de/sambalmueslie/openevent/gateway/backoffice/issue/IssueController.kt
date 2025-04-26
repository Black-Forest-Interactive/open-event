package de.sambalmueslie.openevent.gateway.backoffice.issue

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.core.issue.IssueCrudService
import de.sambalmueslie.openevent.core.issue.api.Issue
import de.sambalmueslie.openevent.core.issue.api.IssueStatus
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/issue")
@Tag(name = "BACKOFFICE Issue API")
class IssueController(
    private val service: IssueCrudService,
    private val accountService: AccountCrudService,
) {

    companion object {
        private const val PERMISSION_ADMIN = "issue.admin"
    }

    @Get("/{id}")
    fun get(auth: Authentication, id: Long): Issue? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.get(id)
        }
    }

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable): Page<Issue> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getAll(pageable)
        }
    }

    @Get("account/{accountId}")
    fun getByAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Issue> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
            service.getByAccount(account, pageable)
        }
    }

    @Get("account/{accountId}/unresolved")
    fun getUnresolvedByAccount(auth: Authentication, accountId: Long, pageable: Pageable): Page<Issue> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val account = accountService.get(accountId) ?: return@checkPermission Page.empty()
            service.getUnresolvedByAccount(account, pageable)
        }
    }

    @Get("unresolved")
    fun getUnresolved(auth: Authentication, pageable: Pageable): Page<Issue> {
        return auth.checkPermission(PERMISSION_ADMIN) {
            service.getUnresolved(pageable)
        }
    }


    @Put("/{id}/status")
    fun changeStatus(auth: Authentication, id: Long, @Body status: PatchRequest<IssueStatus>): Issue? {
        return auth.checkPermission(PERMISSION_ADMIN) {
            val actor = accountService.get(auth) ?: return@checkPermission null
            service.changeStatus(actor, id, status)
        }
    }

}