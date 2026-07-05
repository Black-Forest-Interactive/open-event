package de.sambalmueslie.openevent.gateway.backoffice.mail

import de.sambalmueslie.openevent.infrastructure.mail.api.MailJob
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Put
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/mail")
@Tag(name = "BACKOFFICE Mail API")
class MailController(private val service: MailGuardService) {

    @Get()
    fun getJobs(auth: Authentication, pageable: Pageable) = service.getJobs(auth, pageable)

    @Get("/failed")
    fun getFailedJobs(auth: Authentication, pageable: Pageable) = service.getFailedJobs(auth, pageable)

    @Get("/{jobId}/history")
    fun getJobHistory(auth: Authentication, jobId: Long, pageable: Pageable) = service.getJobHistory(auth, jobId, pageable)

    @Put("{jobId}/retry")
    fun retryJob(auth: Authentication, jobId: Long): MailJob? = service.retryJob(auth, jobId)
}
