package de.sambalmueslie.openevent.core.account.api

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AccountValidationResult(
    val created: Boolean,
    val account: Account,
    val profile: Profile,
    val info: AccountInfo
)
