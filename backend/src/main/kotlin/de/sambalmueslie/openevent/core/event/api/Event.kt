package de.sambalmueslie.openevent.core.event.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.formatRange
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
data class Event(
    override val id: Long,

    val owner: AccountInfo,

    val start: LocalDateTime,
    val finish: LocalDateTime,

    val title: String,
    val shortText: String,
    val longText: String,
    val imageUrl: String,
    val iconUrl: String,

    val hasLocation: Boolean,
    val hasRegistration: Boolean,
    val published: Boolean,

    val tags: Set<String>,

    val created: LocalDateTime,
    val changed: LocalDateTime?
) : BusinessObject<Long> {
    fun format() = formatRange(start, finish)
}
