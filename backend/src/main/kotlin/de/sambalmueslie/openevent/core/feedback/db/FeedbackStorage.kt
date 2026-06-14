package de.sambalmueslie.openevent.core.feedback.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.feedback.api.Feedback
import de.sambalmueslie.openevent.core.feedback.api.FeedbackChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface FeedbackStorage : Storage<Long, Feedback, FeedbackChangeRequest> {
    fun create(request: FeedbackChangeRequest, account: Account, clientIp: String, userAgent: String): Feedback
    fun findByAccount(account: Account, pageable: Pageable): Page<Feedback>
    fun findByTopic(topic: String, pageable: Pageable): Page<Feedback>
    fun getData(id: Long): FeedbackData?

}
