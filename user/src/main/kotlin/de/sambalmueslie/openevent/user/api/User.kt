package de.sambalmueslie.openevent.user.api

import de.sambalmueslie.openevent.common.crud.BusinessObject

data class User(
    override val id: Long,
    val externalId: String,
    val userName: String,
    val email: String,
    val firstName: String,
    val lastName: String,
) : BusinessObject<Long>
