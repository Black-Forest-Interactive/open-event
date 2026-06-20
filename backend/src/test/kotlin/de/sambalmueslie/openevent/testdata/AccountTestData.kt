package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.AccountCrudService
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountChangeRequest
import de.sambalmueslie.openevent.core.account.api.AccountSetupRequest
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest

/**
 * Reusable test data for accounts. Provides request builders plus persist helpers. `createActor`
 * bootstraps the [Account] acting as `actor` in service calls (directly via storage, no profile),
 * while `create` exercises the full [AccountCrudService.create] path (account + profile + preferences).
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

    fun accountRequest(
        name: String = "account",
        iconUrl: String = "account-icon",
        externalId: String? = "account-id",
    ) = AccountChangeRequest(name, iconUrl, externalId)

    fun create(
        service: AccountCrudService,
        actor: Account,
        request: AccountChangeRequest = accountRequest(),
    ): Account = service.create(actor, request)

    fun profileRequest(
        email: String? = "account@example.com",
        firstName: String = "first",
        lastName: String = "last",
        language: String = "en",
    ) = ProfileChangeRequest(email = email, firstName = firstName, lastName = lastName, language = language)

    fun setupRequest(
        account: AccountChangeRequest = accountRequest(),
        profile: ProfileChangeRequest = profileRequest(),
    ) = AccountSetupRequest(account, profile)

}
