package de.sambalmueslie.openevent.core.activity.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
data class Activity(
    override val id: Long,
    val title: String,
    val actor: AccountInfo,
    val source: String,
    val referenceId: Long,
    val type: String,
    val timestamp: LocalDateTime
) : BusinessObject<Long>
