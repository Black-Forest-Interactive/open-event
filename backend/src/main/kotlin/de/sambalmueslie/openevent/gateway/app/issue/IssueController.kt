package de.sambalmueslie.openevent.gateway.app.issue

import de.sambalmueslie.openevent.core.issue.api.IssueChangeRequest
import io.micronaut.http.HttpRequest
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/issue")
@Tag(name = "APP Issue API")
class IssueController(private val service: IssueGuardService) {

    @Post()
    fun create(auth: Authentication, @Body request: IssueChangeRequest, http: HttpRequest<*>) = service.create(auth, request, http)
}
