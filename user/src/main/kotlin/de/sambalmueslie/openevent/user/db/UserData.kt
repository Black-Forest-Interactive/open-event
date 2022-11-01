package de.sambalmueslie.openevent.user.db


import de.sambalmueslie.openevent.common.crud.DataObject
import de.sambalmueslie.openevent.user.api.User
import de.sambalmueslie.openevent.user.api.UserChangeRequest
import de.sambalmueslie.openevent.user.api.UserType
import jakarta.persistence.*
import java.time.LocalDateTime

@Suppress("JpaObjectClassSignatureInspection")
@Entity(name = "User")
@Table(name = "_user")
data class UserData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    @Column
    var externalId: String,
    @Column @Enumerated(EnumType.STRING)
    var type: UserType,
    @Column
    var userName: String,
    @Column
    var firstName: String,
    @Column
    var lastName: String,
    @Column
    var email: String,
    @Column
    var mobile: String,
    @Column
    var phone: String,
    @Column
    var created: LocalDateTime,
    @Column
    var updated: LocalDateTime?,
) : DataObject<User> {

    companion object {
        fun create(request: UserChangeRequest, timestamp: LocalDateTime): UserData {
            return UserData(0, request.externalId, request.type, request.userName, request.firstName, request.lastName, request.email, request.mobile, request.phone, timestamp, null)
        }
    }

    override fun convert() = User(id, externalId, type, userName, firstName, lastName, email, mobile, phone)

    fun update(request: UserChangeRequest, timestamp: LocalDateTime): UserData {
        externalId = request.externalId
        type = request.type
        userName = request.userName
        firstName = request.firstName
        lastName = request.lastName
        email = request.email
        mobile = request.mobile
        phone = request.phone
        updated = timestamp
        return this
    }
}
