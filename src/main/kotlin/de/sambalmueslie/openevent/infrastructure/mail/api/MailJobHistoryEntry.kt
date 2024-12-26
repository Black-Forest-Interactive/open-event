package de.sambalmueslie.openevent.infrastructure.mail.api

import de.sambalmueslie.openevent.common.BusinessObject
import java.time.LocalDateTime

data class MailJobHistoryEntry(
    override val id: Long,
    val message: String,
    val timestamp: LocalDateTime,
) : BusinessObject<Long>
