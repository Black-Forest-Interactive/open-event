package de.sambalmueslie.openevent.core.announcement.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.Account
import java.time.LocalDateTime

data class Announcement(
    override val id: Long,
    val subject: String,
    val content: String,
    val author: Account,
    val timestamp: LocalDateTime
) : BusinessObject<Long>
