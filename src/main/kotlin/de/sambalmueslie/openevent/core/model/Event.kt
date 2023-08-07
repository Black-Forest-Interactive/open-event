package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime
@Serdeable
data class Event(
    override val id: Long,

    val owner: Account,

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
) : BusinessObject<Long>
