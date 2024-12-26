package de.sambalmueslie.openevent.core.search.account

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.search.api.AccountSearchEntry
import de.sambalmueslie.openevent.nullIfBlank
import kotlinx.serialization.Serializable

@Serializable
data class AccountSearchEntryData(
    var id: String,
    var externalId: String?,
    var name: String,

    var email: String?,
    var phone: String?,
    var mobile: String?,

    var firstName: String,
    var lastName: String,
) {
    fun convert(): AccountSearchEntry {
        return AccountSearchEntry(id, name, email ?: "", phone ?: "", mobile ?: "", firstName, lastName)
    }

    companion object {
        fun create(account: Account, profile: Profile?): AccountSearchEntryData {
            return AccountSearchEntryData(
                account.id.toString(),
                account.externalId,
                account.name,
                profile?.email.nullIfBlank(),
                profile?.phone.nullIfBlank(),
                profile?.mobile.nullIfBlank(),
                profile?.firstName ?: account.name,
                profile?.lastName ?: account.name,
            )
        }
    }
}
