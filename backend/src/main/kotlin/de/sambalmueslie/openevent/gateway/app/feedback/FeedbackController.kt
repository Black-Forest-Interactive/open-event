package de.sambalmueslie.openevent.gateway.app.feedback

import de.sambalmueslie.openevent.core.feedback.api.FeedbackChangeRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/feedback")
@Tag(name = "APP Feedback API")
class FeedbackController(private val service: FeedbackGuardService) {
    @Post()
    fun create(auth: Authentication, @Body request: FeedbackChangeRequest, http: HttpRequest<*>): HttpResponse<Any> = service.create(auth, request, http)
}