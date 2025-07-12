package de.sambalmueslie.openevent.core.participant.db

import de.sambalmueslie.openevent.core.event.api.EventInfo
import de.sambalmueslie.openevent.core.formatTimestamp
import de.sambalmueslie.openevent.core.participant.api.ExternalParticipantAddRequest
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "ExternalParticipant")
@Table(name = "participant_external")
data class ExternalParticipantData(
    @Id var id: String,

    @Column var eventId: Long,

    @Column var firstName: String,
    @Column var lastName: String,
    @Column var email: String,
    @Column var phone: String,
    @Column var mobile: String,
    @Column var language: String,
    @Column var size: Long,

    @Column var expires: LocalDateTime,
    @Column var code: String,
    @Column var invalidConfirmationTrials: Int,

    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime?

) {
    companion object {
        fun create(
            id: String,
            event: EventInfo,
            request: ExternalParticipantAddRequest,
            lang: String,
            code: String,
            expires: LocalDateTime,
            timestamp: LocalDateTime
        ): ExternalParticipantData {
            return ExternalParticipantData(
                id, event.event.id,
                request.firstName, request.lastName, request.email, request.phone, request.mobile, lang, request.size,
                expires, code, 0, timestamp, null
            )
        }
    }

    fun updateExpires(expiresTimestamp: LocalDateTime, timestamp: LocalDateTime): ExternalParticipantData {
        this.expires = expiresTimestamp
        this.updated = timestamp
        return this
    }

    fun format() = formatTimestamp(expires)
}
