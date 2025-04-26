package de.sambalmueslie.openevent.core.participant.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantChangeRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipantDetails
import de.sambalmueslie.openevent.core.registration.api.Registration

interface ParticipantStorage : Storage<Long, Participant, ParticipantChangeRequest> {
    fun create(request: ParticipantChangeRequest, author: Account, registration: Registration): Participant
    fun get(registration: Registration): List<Participant>
    fun findByAccount(registration: Registration, account: Account): Participant?
    fun get(registration: List<Registration>): Map<Registration, List<Participant>>
    fun getDetails(registration: Registration): List<ParticipantDetails>
}
