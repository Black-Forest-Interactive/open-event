package de.sambalmueslie.openevent.core.message.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.Account
import java.time.LocalDateTime

data class Message(
    override val id: Long,
    val subject: String,
    val content: String,
    val timestamp: LocalDateTime,
    val author: Account,
) : BusinessObject<Long>
