package de.sambalmueslie.openevent.core.feedback.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.feedback.api.Feedback
import de.sambalmueslie.openevent.core.feedback.api.FeedbackChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class FeedbackStorageService(
    private val repository: FeedbackRepository,
    private val converter: FeedbackConverter,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Feedback, FeedbackChangeRequest, FeedbackData>(
    repository, converter, cacheService, Feedback::class, logger
), FeedbackStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FeedbackStorageService::class.java)
        private const val ACCOUNT_REFERENCE = "account"
        private const val CLIENT_IP_REFERENCE = "clientIp"
        private const val USER_AGENT_REFERENCE = "userAgent"
    }

    override fun create(request: FeedbackChangeRequest, account: Account, clientIp: String, userAgent: String): Feedback {
        return create(
            request, mapOf(
                Pair(ACCOUNT_REFERENCE, account),
                Pair(CLIENT_IP_REFERENCE, clientIp),
                Pair(USER_AGENT_REFERENCE, userAgent),
            )
        )
    }

    override fun createData(request: FeedbackChangeRequest, properties: Map<String, Any>): FeedbackData {
        val account = properties[ACCOUNT_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        val clientIp = properties[CLIENT_IP_REFERENCE] as? String ?: throw InvalidRequestException("Cannot find clientIp")
        val userAgent = properties[USER_AGENT_REFERENCE] as? String ?: throw InvalidRequestException("Cannot find userAgent")
        return FeedbackData.Companion.create(account, request, clientIp, userAgent, timeProvider.now())
    }

    override fun updateData(data: FeedbackData, request: FeedbackChangeRequest): FeedbackData {
        return data.update(request, timeProvider.now())
    }

    override fun findByAccount(account: Account, pageable: Pageable): Page<Feedback> {
        return repository.findByAccountId(account.id, pageable).let { converter.convert(it) }
    }

    override fun findByTopic(topic: String, pageable: Pageable): Page<Feedback> {
        return repository.findByTopic(topic, pageable).let { converter.convert(it) }
    }

    override fun getData(id: Long): FeedbackData? {
        return repository.findByIdOrNull(id)
    }


}
