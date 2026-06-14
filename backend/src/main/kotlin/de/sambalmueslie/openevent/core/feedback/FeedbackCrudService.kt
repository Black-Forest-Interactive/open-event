package de.sambalmueslie.openevent.core.feedback

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.feedback.api.Feedback
import de.sambalmueslie.openevent.core.feedback.api.FeedbackChangeRequest
import de.sambalmueslie.openevent.core.feedback.db.FeedbackStorage
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.HttpRequest
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class FeedbackCrudService(
    private val storage: FeedbackStorage,
) : BaseCrudService<Long, Feedback, FeedbackChangeRequest, FeedbackChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FeedbackCrudService::class.java)
    }

    fun create(actor: Account, account: Account, request: FeedbackChangeRequest, http: HttpRequest<*>): Feedback {
        val clientIp = http.headers["X-Real-IP"] ?: http.headers["X-Forwarded-For"] ?: http.remoteAddress.address.hostAddress
        val userAgent = http.headers["User-Agent"] ?: "unknown"
        val result = storage.create(request, account, clientIp, userAgent)
        notifyCreated(actor, result)
        return result
    }

    override fun isValid(request: FeedbackChangeRequest) {
        // intentionally left empty
    }

    fun getByAccount(account: Account, pageable: Pageable): Page<Feedback> {
        return storage.findByAccount(account, pageable)
    }

    fun getByTopic(topic: String, pageable: Pageable): Page<Feedback> {
        return storage.findByTopic(topic, pageable)
    }

}