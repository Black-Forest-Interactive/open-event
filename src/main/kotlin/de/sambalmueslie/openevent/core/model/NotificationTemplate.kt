package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NotificationTemplate(
    override val id: Long,
    val subject: String,
    val lang: String,
    val content: String,
    val plain: Boolean,
) : BusinessObject<Long>
