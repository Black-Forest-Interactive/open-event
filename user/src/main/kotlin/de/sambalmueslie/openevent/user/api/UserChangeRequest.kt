package de.sambalmueslie.openevent.user.api

import de.sambalmueslie.openevent.common.crud.BusinessObjectChangeRequest

data class UserChangeRequest(
    val externalId: String,
    val userName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
) : BusinessObjectChangeRequest {
}
