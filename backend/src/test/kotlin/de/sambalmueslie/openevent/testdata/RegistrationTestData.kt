package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest

/**
 * Reusable test data for registrations. A registration is auto-created for every event
 * (EventCrudService.create), so tests usually obtain it via RegistrationCrudService.findByEvent.
 */
object RegistrationTestData {

    fun createRequest(maxGuestAmount: Int = 10, interestedAllowed: Boolean = true, ticketsEnabled: Boolean = false) =
        RegistrationChangeRequest(maxGuestAmount, interestedAllowed, ticketsEnabled)

    fun updateRequest(maxGuestAmount: Int = 20, interestedAllowed: Boolean = false, ticketsEnabled: Boolean = true) =
        RegistrationChangeRequest(maxGuestAmount, interestedAllowed, ticketsEnabled)

}
