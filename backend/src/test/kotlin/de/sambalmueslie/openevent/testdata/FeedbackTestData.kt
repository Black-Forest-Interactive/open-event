package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.feedback.FeedbackCrudService
import de.sambalmueslie.openevent.core.feedback.api.Feedback
import de.sambalmueslie.openevent.core.feedback.api.FeedbackChangeRequest
import io.micronaut.http.HttpRequest

object FeedbackTestData {

    fun createRequest(
        subject: String = "subject",
        description: String = "description",
        topic: String = "topic",
        tags: Set<String> = setOf("tag"),
        rating: Int = 5,
    ) = FeedbackChangeRequest(subject, description, topic, tags, rating)

    fun updateRequest(
        subject: String = "subject-update",
        description: String = "description-update",
        topic: String = "topic-update",
        tags: Set<String> = setOf("tag-update"),
        rating: Int = 1,
    ) = FeedbackChangeRequest(subject, description, topic, tags, rating)

    fun create(
        service: FeedbackCrudService,
        actor: Account,
        account: Account,
        request: FeedbackChangeRequest = createRequest(),
        http: HttpRequest<*> = TestHttpRequest.mock(),
    ): Feedback = service.create(actor, account, request, http)

}
