package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.infrastructure.mail.api.MailJob
import de.sambalmueslie.openevent.infrastructure.mail.api.MailJobHistoryEntry
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface MailAPI {
    companion object {
        const val PERMISSION_READ = "openevent.mail.read"
        const val PERMISSION_WRITE = "openevent.mail.write"
    }

    fun getJobs(auth: Authentication, pageable: Pageable): Page<MailJob>
    fun getFailedJobs(auth: Authentication, pageable: Pageable): Page<MailJob>
    fun getJobHistory(auth: Authentication, jobId: Long, pageable: Pageable): Page<MailJobHistoryEntry>

}
