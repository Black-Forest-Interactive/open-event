package de.sambalmueslie.openevent.core.search.api

import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.formatRange
import java.time.LocalDateTime

data class EventSearchEntry(
    // event data
    val id: String,
    val created: LocalDateTime,
    val updated: LocalDateTime,
    val start: LocalDateTime,
    val finish: LocalDateTime,

    val title: String,
    val shortText: String,
    val longText: String,
    val published: Boolean,
    val shared: Boolean,

    val owner: AccountInfo,

    // location data
    val hasLocation: Boolean,
    val street: String,
    val streetNumber: String,
    val zip: String,
    val city: String,
    val country: String,

    val lat: Double,
    val lon: Double,

    // registration data
    val hasSpaceLeft: Boolean,
    val maxGuestAmount: Int,
    val amountAccepted: Int,
    val amountOnWaitingList: Int,
    val remainingSpace: Int,

    // user specific flags
    val ownEvent: Boolean,
    val participatingEvent: Boolean,

    // categories
    val categories: Set<String>,
    val tags: Set<String>,
) {
    fun format() = formatRange(start, finish)
}
