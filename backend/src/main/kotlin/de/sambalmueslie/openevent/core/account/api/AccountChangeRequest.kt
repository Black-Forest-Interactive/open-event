package de.sambalmueslie.openevent.core.account.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AccountChangeRequest(
    val name: String,
    val iconUrl: String,
    val externalId: String?,
) : BusinessObjectChangeRequest
