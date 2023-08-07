package de.sambalmueslie.openevent.core.model

import de.sambalmueslie.openevent.core.BusinessObject
import io.micronaut.serde.annotation.Serdeable

@Serdeable
data class Account(
    override val id: Long,
    val externalId: String?,
    val name: String,
    val firstName: String,
    val lastName: String,
    val email: String,
    val iconUrl: String,
    val serviceAccount: Boolean,
) : BusinessObject<Long> {


    fun getTitle(): String {
        return when {
            firstName.isNotBlank() && lastName.isNotBlank() -> "$firstName $lastName"
            else -> name
        }
    }
}
