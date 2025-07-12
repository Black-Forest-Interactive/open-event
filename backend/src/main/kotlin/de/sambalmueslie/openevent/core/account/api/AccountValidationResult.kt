package de.sambalmueslie.openevent.core.account.api

data class AccountValidationResult(
    val created: Boolean,
    val account: Account,
    val profile: Profile,
    val info: AccountInfo
)
