package de.sambalmueslie.openevent.core.share.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
data class Share(
    override val id: String,
    val eventId: Long,
    val published: Boolean,
    val url: String,

    val owner: AccountInfo,
    val created: LocalDateTime,
    val changed: LocalDateTime?
) : BusinessObject<String>
