package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.ProfileCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest

/**
 * Reusable test data for profiles. A profile is keyed by its account (profile.id == account.id), so the
 * persist helper takes the owning [Account]. Profile email is unique in the schema — use distinct emails
 * when creating several profiles in one test.
 */
object ProfileTestData {

    fun createRequest(
        email: String? = "profile@example.com",
        phone: String? = null,
        firstName: String = "first",
        lastName: String = "last",
        language: String = "en",
    ) = ProfileChangeRequest(email = email, phone = phone, firstName = firstName, lastName = lastName, language = language)

    fun updateRequest(
        email: String? = "profile-update@example.com",
        firstName: String = "first-update",
        lastName: String = "last-update",
        language: String = "de",
    ) = ProfileChangeRequest(email = email, firstName = firstName, lastName = lastName, language = language)

    fun create(
        service: ProfileCrudService,
        actor: Account,
        account: Account,
        request: ProfileChangeRequest = createRequest(),
    ): Profile = service.create(actor, account, request)

}
