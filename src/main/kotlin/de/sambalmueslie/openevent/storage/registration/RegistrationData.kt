package de.sambalmueslie.openevent.storage.registration


import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.Registration
import de.sambalmueslie.openevent.core.model.RegistrationChangeRequest
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Registration")
@Table(name = "registration")
data class RegistrationData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,

    @Column var eventId: Long,

    @Column var maxGuestAmount: Int,
    @Column var interestedAllowed: Boolean,
    @Column var ticketsEnabled: Boolean,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Registration> {
    companion object {
        fun create(
            event: Event,
            request: RegistrationChangeRequest,
            timestamp: LocalDateTime
        ): RegistrationData {
            return RegistrationData(
                0,
                event.id,
                request.maxGuestAmount,
                request.interestedAllowed,
                request.ticketsEnabled,
                timestamp
            )
        }
    }

    override fun convert(): Registration {
        return Registration(id, eventId, maxGuestAmount, interestedAllowed, ticketsEnabled)
    }

    fun update(request: RegistrationChangeRequest, timestamp: LocalDateTime): RegistrationData {
        maxGuestAmount = request.maxGuestAmount
        interestedAllowed = request.interestedAllowed
        ticketsEnabled = request.ticketsEnabled
        updated = timestamp
        return this
    }
}


