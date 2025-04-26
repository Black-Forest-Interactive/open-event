package de.sambalmueslie.openevent.gateway.external.event

import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.search.event.EventSearchEntryData
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.gateway.external.account.PublicAccount
import de.sambalmueslie.openevent.gateway.external.account.toPublicAccount
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

fun EventInfo.toPublicEvent(share: Share): PublicEvent {
    val entry = EventSearchEntryData.create(this)
    return PublicEvent(
        share.id,
        event.start,
        event.finish,
        event.title,
        event.shortText,
        event.longText,
        event.owner.toPublicAccount(),

        event.hasLocation,
        entry.zip ?: "",
        entry.city ?: "",
        entry.country ?: "",

        entry.hasSpaceLeft,
        entry.maxGuestAmount,
        entry.amountAccepted,
        entry.amountOnWaitingList,
        entry.remainingSpace,

        entry.categories,
        entry.tags
    )
}