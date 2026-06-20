package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest

/**
 * Reusable test data for participants. Participants are created/changed through
 * ParticipantCrudService.change (or RegistrationCrudService.addParticipant), not a plain create.
 */
object ParticipantTestData {

    fun participateRequest(size: Long = 2, note: String = "note") = ParticipateRequest(size, note)

}
