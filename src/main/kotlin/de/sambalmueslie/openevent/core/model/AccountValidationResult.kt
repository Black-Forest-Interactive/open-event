package de.sambalmueslie.openevent.core.model

import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AccountValidationResult(
    val created: Boolean,
    val account: Account
)
