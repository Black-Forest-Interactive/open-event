package de.sambalmueslie.openevent.core.link.api

import de.sambalmueslie.openevent.common.BusinessObject
import java.time.LocalDateTime

data class Link(
    override val id: String,
    val key: String,
    val enabled: Boolean,
    val params: Map<String, String>,

    val created: LocalDateTime,
    val changed: LocalDateTime?
) : BusinessObject<String>
