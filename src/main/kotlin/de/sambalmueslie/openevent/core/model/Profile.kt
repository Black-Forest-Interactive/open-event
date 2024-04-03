package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable
import java.time.LocalDate
@Serdeable
data class Profile(
    override val id: Long,

    val email: String,
    val phone: String,
    val mobile: String,

    val firstName: String,
    val lastName: String,

    val dateOfBirth: LocalDate,
    val gender: String,
    val profilePicture: String,
    val website: String

) : BusinessObject<Long> {


    fun getTitle(): String {
        return when {
            firstName.isNotBlank() && lastName.isNotBlank() -> "$firstName $lastName"
//            else -> name
            else -> ""
        }
    }
}
