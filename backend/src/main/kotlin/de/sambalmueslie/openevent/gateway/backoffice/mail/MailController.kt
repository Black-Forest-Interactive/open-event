package de.sambalmueslie.openevent.gateway.backoffice.mail

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.mail.MailService
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/mail")
@Tag(name = "BACKOFFICE Mail API")
class MailController(private val service: MailService) {
    companion object {
        private const val PERMISSION_READ = "mail.read"
        private const val PERMISSION_WRITE = "mail.write"
        private const val PERMISSION_ADMIN = "mail.admin"
    }

    @Get()
    fun getJobs(auth: Authentication, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { service.getJobs(pageable) }

    @Get("/failed")
    fun getFailedJobs(auth: Authentication, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { service.getFailedJobs(pageable) }

    @Get("/{jobId}/history")
    fun getJobHistory(auth: Authentication, jobId: Long, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { service.getJobHistory(jobId, pageable) }

}