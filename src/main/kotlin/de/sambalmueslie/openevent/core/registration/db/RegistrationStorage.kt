package de.sambalmueslie.openevent.core.registration.db

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.registration.api.Registration
import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest

interface RegistrationStorage : Storage<Long, Registration, RegistrationChangeRequest> {
    fun create(request: RegistrationChangeRequest, event: Event): Registration
    fun findByEvent(event: Event): Registration?
    fun findByEventIds(eventIds: Set<Long>): List<Registration>
}
