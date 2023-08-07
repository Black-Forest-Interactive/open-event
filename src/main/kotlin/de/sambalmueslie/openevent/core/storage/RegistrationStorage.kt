package de.sambalmueslie.openevent.core.storage

import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.core.model.Registration
import de.sambalmueslie.openevent.core.model.RegistrationChangeRequest

interface RegistrationStorage : Storage<Long, Registration, RegistrationChangeRequest> {
    fun create(request: RegistrationChangeRequest, event: Event): Registration
    fun findByEvent(event: Event): Registration?
    fun findByEventIds(eventIds: Set<Long>): List<Registration>
}
