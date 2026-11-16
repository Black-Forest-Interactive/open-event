package de.sambalmueslie.openevent.core.event.contact

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity(name = "EventContact")
@Table(name = "event_contact")
data class EventContactData(
    @Id var eventId: Long,
    @Column var phone: String,
    @Column var mobile: String,

    @Column var created: LocalDateTime,
    @Column var updated: LocalDateTime? = null
) {
    fun convert() = EventContact(eventId, phone, mobile)

    fun update(request: EventContactChangeRequest, timestamp: LocalDateTime): EventContactData {
        phone = request.phone.trim()
        mobile = request.mobile.trim()
        updated = timestamp
        return this
    }
}
