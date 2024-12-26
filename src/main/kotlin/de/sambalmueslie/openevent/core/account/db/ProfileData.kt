package de.sambalmueslie.openevent.core.account.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest
import de.sambalmueslie.openevent.nullIfBlank
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(name = "Profile")
@Table(name = "profile")
data class ProfileData(
    @Id var id: Long,

    @Column var email: String?,
    @Column var phone: String?,
    @Column var mobile: String?,

    @Column var firstName: String,
    @Column var lastName: String,

    @Column var dateOfBirth: LocalDate?,
    @Column var gender: String?,
    @Column var profilePicture: String?,
    @Column var website: String?,

    @Column var language: String,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Profile> {

    companion object {
        fun create(
            account: Account,
            request: ProfileChangeRequest,
            timestamp: LocalDateTime
        ): ProfileData {
            return ProfileData(
                account.id,
                request.email.nullIfBlank(),
                request.phone.nullIfBlank(),
                request.mobile.nullIfBlank(),
                request.firstName,
                request.lastName,
                request.dateOfBirth,
                request.gender.nullIfBlank(),
                request.profilePicture.nullIfBlank(),
                request.website.nullIfBlank(),
                request.language,
                timestamp
            )
        }
    }

    override fun convert(): Profile {
        return Profile(
            id,
            email,
            phone,
            mobile,
            firstName,
            lastName,
            dateOfBirth,
            gender,
            profilePicture,
            website,
            language
        )
    }

    fun update(request: ProfileChangeRequest, timestamp: LocalDateTime): ProfileData {
        email = request.email.nullIfBlank()
        phone = request.phone.nullIfBlank()
        mobile = request.mobile.nullIfBlank()
        firstName = request.firstName
        lastName = request.lastName
        dateOfBirth = request.dateOfBirth
        gender = request.gender.nullIfBlank()
        profilePicture = request.profilePicture.nullIfBlank()
        website = request.website.nullIfBlank()
        language = request.language
        updated = timestamp
        return this
    }
}
