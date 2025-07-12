package de.sambalmueslie.openevent.core.issue.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.Account
import java.time.LocalDateTime

data class Issue(
    override val id: Long,
    val subject: String,
    val description: String,

    val error: String,
    val url: String,


    val account: Account,
    val status: IssueStatus,

    val clientIp: String,
    val userAgent: String,

    val timestamp: LocalDateTime
) : BusinessObject<Long>
