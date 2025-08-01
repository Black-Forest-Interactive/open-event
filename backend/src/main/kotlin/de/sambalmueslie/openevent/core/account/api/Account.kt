package de.sambalmueslie.openevent.core.account.api

import de.sambalmueslie.openevent.common.BusinessObject
import java.time.LocalDateTime


data class Account(
    override val id: Long,
    val externalId: String?,
    val name: String,
    val iconUrl: String,

    val registrationDate: LocalDateTime,
    val lastLoginDate: LocalDateTime?,

    val serviceAccount: Boolean,
    val idpLinked: Boolean,
) : BusinessObject<Long>
