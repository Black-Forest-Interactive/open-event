package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountChangeRequest

/**
 * Reusable test data for accounts. Used to create the [Account] acting as `actor` in service calls.
 */
object AccountTestData {

    fun createRequest(
        name: String = "user",
        iconUrl: String = "",
        externalId: String? = "actor-id",
    ) = AccountChangeRequest(name, iconUrl, externalId)

    fun createActor(
        storage: AccountStorage,
        request: AccountChangeRequest = createRequest(),
    ): Account = storage.create(request)

}
