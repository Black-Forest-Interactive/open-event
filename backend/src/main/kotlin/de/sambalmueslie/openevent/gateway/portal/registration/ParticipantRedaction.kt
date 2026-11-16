package de.sambalmueslie.openevent.gateway.portal.registration

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.gateway.portal.event.redactOwnerFor

private val ANONYMOUS = AccountInfo(0, "", "", "", "", "", "")

/**
 * Personal data of other participants is only visible to the organizer. Everybody else gets the own entry
 * and anonymous entries (size / waiting list / rank) to calculate the occupancy. The email of the organizer is removed too.
 */
fun List<Participant>.redactFor(accountId: Long): List<Participant> =
    map { if (it.author.id == accountId) it else it.copy(note = "", author = ANONYMOUS) }

fun EventInfo.redactFor(account: Account): EventInfo =
    if (event.owner.id == account.id) this
    else copy(event = event.redactOwnerFor(account), registration = registration?.copy(participants = registration.participants.redactFor(account.id)))

fun ParticipateResponse.redactFor(account: Account): ParticipateResponse =
    copy(participants = participants.redactFor(account.id))
