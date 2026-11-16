package de.sambalmueslie.openevent.core.event.contact

data class EventContact(
    val eventId: Long,
    val phone: String,
    val mobile: String,
) {
    fun isEmpty() = phone.isBlank() && mobile.isBlank()
}
