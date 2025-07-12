package de.sambalmueslie.openevent.core.issue

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.issue.api.Issue
import de.sambalmueslie.openevent.core.issue.api.IssueChangeRequest
import de.sambalmueslie.openevent.core.issue.api.IssueStatus
import de.sambalmueslie.openevent.core.issue.db.IssueStorage
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class IssueCrudService(
    private val storage: IssueStorage,
) : BaseCrudService<Long, Issue, IssueChangeRequest, IssueChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(IssueCrudService::class.java)
    }

    fun create(actor: Account, account: Account, request: IssueChangeRequest, http: HttpRequest<*>): Issue {
        val clientIp = http.headers["X-Real-IP"] ?: http.headers["X-Forwarded-For"] ?: http.remoteAddress.address.hostAddress
        val userAgent = http.headers["User-Agent"] ?: "unknown"
        val result = storage.create(request, account, clientIp, userAgent)
        notifyCreated(actor, result)
        return result
    }

    override fun isValid(request: IssueChangeRequest) {
        // intentionally left empty
    }

    fun getByAccount(account: Account, pageable: Pageable): Page<Issue> {
        return storage.findByAccount(account, pageable)
    }

    fun getUnresolved(pageable: Pageable): Page<Issue> {
        return storage.getUnresolved(pageable)
    }

    fun getUnresolvedByAccount(account: Account, pageable: Pageable): Page<Issue> {
        return storage.getUnresolvedByAccount(account, pageable)
    }

    fun changeStatus(actor: Account, id: Long, status: PatchRequest<IssueStatus>): Issue? {
        val result = storage.updateStatus(id, status) ?: return null
        notifyUpdated(actor, result)
        return result
    }


}