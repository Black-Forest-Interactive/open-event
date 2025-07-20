package de.sambalmueslie.openevent.core.feedback.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.Account
import java.time.LocalDateTime

data class Feedback(
    override val id: Long,
    val subject: String,
    val description: String,
    val topic: String,
    val tags: Set<String>,
    val rating: Int,

    val account: Account,

    val clientIp: String,
    val userAgent: String,

    val timestamp: LocalDateTime
) : BusinessObject<Long>
