package de.sambalmueslie.openevent.gateway.external.account

import de.sambalmueslie.openevent.core.account.api.AccountInfo
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class PublicAccount (
    val name: String,
)

fun AccountInfo.toPublicAccount(): PublicAccount {
    return PublicAccount(name)
}