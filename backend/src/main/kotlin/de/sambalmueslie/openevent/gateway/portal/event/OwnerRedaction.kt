package de.sambalmueslie.openevent.gateway.portal.event

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.search.api.EventSearchEntry
import de.sambalmueslie.openevent.core.search.api.EventSearchResponse

/**
 * The email of the organizer is only visible to the organizer itself. Participants get in touch via the
 * contact data the organizer provides for the event.
 */
fun Event.redactOwnerFor(account: Account): Event =
    if (owner.id == account.id) this else copy(owner = owner.copy(email = ""))

fun EventSearchEntry.redactOwnerFor(account: Account): EventSearchEntry =
    if (owner.id == account.id) this else copy(owner = owner.copy(email = ""))

fun EventSearchResponse.redactOwnerFor(account: Account): EventSearchResponse =
    copy(result = result.map { it.redactOwnerFor(account) })
