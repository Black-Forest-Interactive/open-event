package de.sambalmueslie.openevent.storage.account

import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.AccountChangeRequest
import de.sambalmueslie.openevent.storage.SimpleDataObject
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Account")
@Table(name = "account")
data class AccountData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var externalId: String?,
    @Column var name: String,
    @Column var firstName: String,
    @Column var lastName: String,
    @Column var email: String,
    @Column var iconUrl: String,
    @Column var serviceAccount: Boolean,

    @Column var lastSync: LocalDateTime = LocalDateTime.now(),
    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Account> {

    companion object {
        fun create(
            request: AccountChangeRequest,
            timestamp: LocalDateTime
        ): AccountData {
            return AccountData(
                0,
                request.externalId,
                request.name,
                request.firstName,
                request.lastName,
                request.email,
                request.iconUrl,
                false,
                timestamp,
                timestamp
            )
        }
    }

    override fun convert(): Account {
        return Account(id, externalId, name, firstName, lastName, email, iconUrl, serviceAccount)
    }

    fun update(request: AccountChangeRequest, timestamp: LocalDateTime): AccountData {
        externalId = request.externalId
        name = request.name
        firstName = request.firstName
        lastName = request.lastName
        email = request.email
        iconUrl = request.iconUrl
        updated = timestamp
        return this
    }

}
