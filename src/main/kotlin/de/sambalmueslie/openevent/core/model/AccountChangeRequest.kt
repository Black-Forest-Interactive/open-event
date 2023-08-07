package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class AccountChangeRequest(
    val name: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val iconUrl: String,
    val externalId: String?,
) : BusinessObjectChangeRequest
