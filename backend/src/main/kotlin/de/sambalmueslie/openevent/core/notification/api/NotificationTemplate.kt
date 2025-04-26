package de.sambalmueslie.openevent.core.notification.api

import de.sambalmueslie.openevent.common.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class NotificationTemplate(
    override val id: Long,
    val subject: String,
    val lang: String,
    val content: String,
    val plain: Boolean,
) : BusinessObject<Long>
