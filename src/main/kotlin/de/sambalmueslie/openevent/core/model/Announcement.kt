package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime
@Serdeable
data class Announcement(
    override val id: Long,
    val subject: String,
    val content: String,
    val author: Account,
    val timestamp: LocalDateTime
) : BusinessObject<Long>
