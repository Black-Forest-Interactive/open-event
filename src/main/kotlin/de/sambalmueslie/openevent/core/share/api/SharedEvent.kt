package de.sambalmueslie.openevent.core.share.api

import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
data class SharedEvent(
    val id: Long,

    val owner: SharedAccount,

    val start: LocalDateTime,
    val finish: LocalDateTime,

    val title: String,
    val shortText: String,
    val longText: String,
    val imageUrl: String,
    val iconUrl: String,

    val published: Boolean,
)
