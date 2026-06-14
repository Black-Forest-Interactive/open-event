package de.sambalmueslie.openevent.gateway.external.event

import de.sambalmueslie.openevent.gateway.external.account.PublicAccount
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDateTime

@Serdeable
data class PublicEvent(
    val key: String,

    val start: LocalDateTime,
    val finish: LocalDateTime,

    val title: String,
    val shortText: String,
    val longText: String,

    val owner: PublicAccount,

    // location data
    val hasLocation: Boolean,
    val zip: String,
    val city: String,
    val country: String,

    // registration data
    val hasSpaceLeft: Boolean,
    val maxGuestAmount: Int,
    val amountAccepted: Int,
    val amountOnWaitingList: Int,
    val remainingSpace: Int,

    // categories
    val categories: Set<String>,
    val tags: Set<String>,
)
