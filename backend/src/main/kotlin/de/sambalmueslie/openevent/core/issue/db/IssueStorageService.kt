package de.sambalmueslie.openevent.core.issue.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.issue.api.Issue
import de.sambalmueslie.openevent.core.issue.api.IssueChangeRequest
import de.sambalmueslie.openevent.core.issue.api.IssueStatus
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class IssueStorageService(
    private val repository: IssueRepository,
    private val converter: IssueConverter,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Issue, IssueChangeRequest, IssueData>(
    repository, converter, cacheService, Issue::class, logger
), IssueStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(IssueStorageService::class.java)
        private const val ACCOUNT_REFERENCE = "account"
        private const val CLIENT_IP_REFERENCE = "clientIp"
        private const val USER_AGENT_REFERENCE = "userAgent"
        private val unresolvedStatus = setOf(IssueStatus.CREATED, IssueStatus.IN_PROGRESS)
    }

    override fun create(request: IssueChangeRequest, account: Account, clientIp: String, userAgent: String): Issue {
        return create(
            request, mapOf(
                Pair(ACCOUNT_REFERENCE, account),
                Pair(CLIENT_IP_REFERENCE, clientIp),
                Pair(USER_AGENT_REFERENCE, userAgent),
            )
        )
    }

    override fun createData(request: IssueChangeRequest, properties: Map<String, Any>): IssueData {
        val account = properties[ACCOUNT_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        val clientIp = properties[CLIENT_IP_REFERENCE] as? String ?: throw InvalidRequestException("Cannot find clientIp")
        val userAgent = properties[USER_AGENT_REFERENCE] as? String ?: throw InvalidRequestException("Cannot find userAgent")
        return IssueData.Companion.create(account, request, clientIp, userAgent, timeProvider.now())
    }

    override fun updateData(data: IssueData, request: IssueChangeRequest): IssueData {
        return data.update(request, timeProvider.now())
    }


    override fun findByAccount(account: Account, pageable: Pageable): Page<Issue> {
        return repository.findByAccountId(account.id, pageable).let { converter.convert(it) }
    }

    override fun getData(id: Long): IssueData? {
        return repository.findByIdOrNull(id)
    }

    override fun getUnresolved(pageable: Pageable): Page<Issue> {
        return repository.findByStatusIn(unresolvedStatus, pageable).let { converter.convert(it) }
    }

    override fun getUnresolvedByAccount(account: Account, pageable: Pageable): Page<Issue> {
        return repository.findByStatusInAndAccountId(unresolvedStatus, account.id, pageable).let { converter.convert(it) }
    }

    override fun updateStatus(id: Long, status: PatchRequest<IssueStatus>): Issue? {
        return patchData(id) { it.status(status.value, timeProvider.now()) }
    }

}
