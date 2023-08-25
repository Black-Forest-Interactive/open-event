package de.sambalmueslie.openevent.core.logic.registration

import de.sambalmueslie.openevent.core.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.Participant
import de.sambalmueslie.openevent.core.model.ParticipateStatus
import de.sambalmueslie.openevent.core.model.Registration

interface RegistrationChangeListener : BusinessObjectChangeListener<Long, Registration> {
    fun participantChanged(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    )

    fun participantRemoved(actor: Account, registration: Registration, participant: Participant)
}
