package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime
@Serdeable
data class Message(
    override val id: Long,
    val subject: String,
    val content: String,
    val timestamp: LocalDateTime,
    val author: Account,
) : BusinessObject<Long>
