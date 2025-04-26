package de.sambalmueslie.openevent.core.account.api

import de.sambalmueslie.openevent.common.BusinessObjectChangeRequest
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDate

@Serdeable
data class ProfileChangeRequest(

    val email: String? = null,
    val phone: String? = null,
    val mobile: String? = null,

    val firstName: String,
    val lastName: String,

    val dateOfBirth: LocalDate? = null,
    val gender: String? = null,
    val profilePicture: String? = null,
    val website: String? = null,

    val language: String

) : BusinessObjectChangeRequest