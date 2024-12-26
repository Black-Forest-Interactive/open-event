package de.sambalmueslie.openevent.core.account

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest

interface ProfileStorage : Storage<Long, Profile, ProfileChangeRequest> {
    fun create(request: ProfileChangeRequest, account: Account): Profile
    fun findByAccount(account: Account): Profile?
    fun getForAccounts(accounts: Collection<Account>): List<Profile>
    fun findByIdIn(ids: Set<Long>): List<Profile>
}
