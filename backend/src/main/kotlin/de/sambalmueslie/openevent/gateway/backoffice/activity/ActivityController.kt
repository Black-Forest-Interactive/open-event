package de.sambalmueslie.openevent.gateway.backoffice.activity

import de.sambalmueslie.openevent.core.activity.api.ActivityCleanupRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/activity")
@Tag(name = "BACKOFFICE Activity API")
class ActivityController(private val service: ActivityGuardService) {

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)

    @Get("{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Post("cleanup")
    fun cleanup(auth: Authentication, @Body request: ActivityCleanupRequest) = service.cleanup(auth, request)

    @Get("{accountId}/recent")
    fun getRecentForAccount(auth: Authentication, accountId: Long, pageable: Pageable) = service.getRecentForAccount(auth, accountId, pageable)
}
