package de.sambalmueslie.openevent.core.account.api

data class AccountSetupRequest(
    val account: AccountChangeRequest,
    val profile: ProfileChangeRequest
)
