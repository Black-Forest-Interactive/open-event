package de.sambalmueslie.openevent.core.event.contact

import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton

/**
 * Contact data (phone / mobile) of the organizer for a single event.
 * Kept separate from the event itself, so it never ends up in public listings or the search index.
 */
@Singleton
class EventContactService(
    private val repository: EventContactRepository,
    private val timeProvider: TimeProvider,
) {
    companion object {
        private const val MAX_LENGTH = 50
        private val VALID_PATTERN = Regex("^[0-9+()/\\-. ]*$")
    }

    fun get(eventId: Long): EventContact? = repository.findById(eventId).orElse(null)?.convert()

    fun validate(request: EventContactChangeRequest) {
        validate(request.phone)
        validate(request.mobile)
    }

    fun set(eventId: Long, request: EventContactChangeRequest): EventContact? {
        validate(request)
        val existing = repository.findById(eventId).orElse(null)
        if (request.phone.isBlank() && request.mobile.isBlank()) {
            existing?.let { repository.delete(it) }
            return null
        }
        val timestamp = timeProvider.now()
        if (existing != null) return repository.update(existing.update(request, timestamp)).convert()
        val data = EventContactData(eventId, request.phone.trim(), request.mobile.trim(), timestamp)
        return repository.save(data).convert()
    }

    fun delete(eventId: Long) {
        repository.deleteById(eventId)
    }

    private fun validate(value: String) {
        if (value.length > MAX_LENGTH) throw InvalidRequestException("Phone number too long")
        if (!VALID_PATTERN.matches(value)) throw InvalidRequestException("Invalid phone number")
    }
}
