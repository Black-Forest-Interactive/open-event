package de.sambalmueslie.openevent.core.share.api

import de.sambalmueslie.openevent.common.BusinessObject
import java.time.LocalDateTime

data class Share(
    override val id: String,
    val eventId: Long,
    val enabled: Boolean,

    val created: LocalDateTime,
    val changed: LocalDateTime?
) : BusinessObject<String>
