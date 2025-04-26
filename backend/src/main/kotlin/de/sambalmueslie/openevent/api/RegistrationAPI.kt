package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.participant.api.Participant
import de.sambalmueslie.openevent.core.participant.api.ParticipantAddRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateRequest
import de.sambalmueslie.openevent.core.participant.api.ParticipateResponse
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest
import de.sambalmueslie.openevent.core.registration.api.RegistrationDetails
import de.sambalmueslie.openevent.core.registration.api.RegistrationInfo
import io.micronaut.security.authentication.Authentication

interface RegistrationAPI : CrudAPI<Long, Registration, RegistrationChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.registration.read"
        const val PERMISSION_WRITE = "openevent.registration.write"
        const val PERMISSION_MANAGE = "openevent.registration.manage"
        const val PERMISSION_ADMIN = "openevent.registration.admin"
    }

    fun getParticipants(auth: Authentication, id: Long): List<Participant>

    fun addParticipant(auth: Authentication, id: Long, request: ParticipateRequest): ParticipateResponse?

    fun addParticipant(
        auth: Authentication,
        id: Long,
        accountId: Long,
        request: ParticipateRequest
    ): ParticipateResponse?


    fun addParticipant(auth: Authentication, id: Long, request: ParticipantAddRequest): ParticipateResponse?
    fun changeParticipant(auth: Authentication, id: Long, request: ParticipateRequest): ParticipateResponse?
    fun changeParticipant(
        auth: Authentication,
        id: Long,
        participantId: Long,
        request: ParticipateRequest
    ): ParticipateResponse?

    fun removeParticipant(auth: Authentication, id: Long, participantId: Long): ParticipateResponse?
    fun removeParticipant(auth: Authentication, id: Long): ParticipateResponse?

    fun getInfo(auth: Authentication, id: Long): RegistrationInfo?

    fun getDetails(auth: Authentication, id: Long): RegistrationDetails?

}
