package de.sambalmueslie.openevent.infrastructure.mail


import de.sambalmueslie.openevent.api.MailAPI
import de.sambalmueslie.openevent.api.MailAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.core.auth.checkPermission
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/mail")
@Tag(name = "Mail API")
class MailController(private val service: MailService) : MailAPI {

    @Get()
    override fun getJobs(auth: Authentication, pageable: Pageable) =
        auth.checkPermission(PERMISSION_READ) { service.getJobs(pageable) }

    @Get("/failed")
    override fun getFailedJobs(auth: Authentication, pageable: Pageable) =
        auth.checkPermission(PERMISSION_READ) { service.getFailedJobs(pageable) }

    @Get("/{jobId}/history")
    override fun getJobHistory(auth: Authentication, jobId: Long, pageable: Pageable) =
        auth.checkPermission(PERMISSION_READ) { service.getJobHistory(jobId, pageable) }


}
