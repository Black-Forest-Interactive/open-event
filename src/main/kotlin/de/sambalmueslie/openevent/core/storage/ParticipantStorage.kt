package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Participant
import de.sambalmueslie.openevent.core.model.ParticipantChangeRequest
import de.sambalmueslie.openevent.core.model.Registration

interface ParticipantStorage : Storage<Long, Participant, ParticipantChangeRequest> {
    fun create(request: ParticipantChangeRequest, author: Account, registration: Registration): Participant
    fun get(registration: Registration): List<Participant>
}
