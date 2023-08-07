package de.sambalmueslie.openevent.infrastructure.mail.api

import de.sambalmueslie.openevent.core.BusinessObject
import java.time.LocalDateTime

data class MailJob(
    override val id: Long,
    val title: String,
    val status: MailJobStatus,
    val timestamp: LocalDateTime,
) : BusinessObject<Long>
