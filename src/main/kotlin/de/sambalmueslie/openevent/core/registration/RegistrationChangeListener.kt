package de.sambalmueslie.openevent.core.registration

import de.sambalmueslie.openevent.common.BusinessObjectChangeListener
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipateStatus
import de.sambalmueslie.openevent.core.registration.api.Registration

interface RegistrationChangeListener : BusinessObjectChangeListener<Long, Registration> {
    fun participantCreated(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    )

    fun participantChanged(
        actor: Account,
        registration: Registration,
        participant: Participant,
        status: ParticipateStatus
    )

    fun participantRemoved(actor: Account, registration: Registration, participant: Participant)
}
