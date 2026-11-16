package de.sambalmueslie.openevent.gateway.backoffice.mail

import de.sambalmueslie.openevent.core.checkPermission
import de.sambalmueslie.openevent.infrastructure.mail.MailService
import de.sambalmueslie.openevent.infrastructure.mail.api.MailJobStatus
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton

@Singleton
class MailGuardService(private val service: MailService) {
    companion object {
        private const val PERMISSION_ADMIN = "mail.admin"
    }

    fun getJobs(auth: Authentication, search: String?, status: MailJobStatus?, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { service.getJobs(search, status, pageable) }

    fun getFailedJobs(auth: Authentication, pageable: Pageable) = auth.checkPermission(PERMISSION_ADMIN) { service.getFailedJobs(pageable) }

    fun getJobHistory(auth: Authentication, jobId: Long, pageable: Pageable) =
        auth.checkPermission(PERMISSION_ADMIN) { service.getJobHistory(jobId, pageable) }

    fun retryJob(auth: Authentication, jobId: Long) = auth.checkPermission(PERMISSION_ADMIN) { service.retryJob(jobId) }
}
