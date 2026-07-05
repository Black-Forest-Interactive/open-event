package de.sambalmueslie.openevent.gateway.backoffice.feedback

import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/feedback")
@Tag(name = "BACKOFFICE Feedback API")
class FeedbackController(private val service: FeedbackGuardService) {

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)

    @Get("{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get("/by/topic/{topic}")
    fun getByTopic(auth: Authentication, topic: String, pageable: Pageable) = service.getByTopic(auth, topic, pageable)

    @Get("/by/account/{accountId}")
    fun getByAccount(auth: Authentication, accountId: Long, pageable: Pageable) = service.getByAccount(auth, accountId, pageable)
}
