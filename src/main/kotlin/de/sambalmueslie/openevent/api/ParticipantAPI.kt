package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.Participant
import de.sambalmueslie.openevent.core.model.ParticipantChangeRequest

interface ParticipantAPI : CrudAPI<Long, Participant, ParticipantChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.participant.read"
        const val PERMISSION_WRITE = "openevent.participant.write"
        const val PERMISSION_ADMIN = "openevent.participant.admin"
    }



}
