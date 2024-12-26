package de.sambalmueslie.openevent.core.account.api

import de.sambalmueslie.openevent.common.BusinessObject
import de.sambalmueslie.openevent.core.account.db.AccountData

data class AccountDetails(
    override val id: Long,
    val name: String,
    val iconUrl: String,
    val email: String,
    val phone: String,
    val mobile: String,
    val firstName: String,
    val lastName: String,
    val language: String,
) : BusinessObject<Long> {
    companion object {
        fun create(account: Account, profile: Profile?) = AccountDetails(
            account.id,
            account.name,
            account.iconUrl,
            profile?.email ?: "",
            profile?.phone ?: "",
            profile?.mobile ?: "",
            profile?.firstName ?: "",
            profile?.lastName ?: "",
            profile?.language ?: ""
        )

        fun create(account: AccountData, profile: Profile?) = AccountDetails(
            account.id,
            account.name,
            account.iconUrl,
            profile?.email ?: "",
            profile?.phone ?: "",
            profile?.mobile ?: "",
            profile?.firstName ?: "",
            profile?.lastName ?: "",
            profile?.language ?: ""
        )
    }

    fun getTitle(): String {
        return when {
            firstName.isNotBlank() && lastName.isNotBlank() -> "$firstName $lastName"
            else -> name
        }
    }
}
