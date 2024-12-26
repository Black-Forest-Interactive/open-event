package de.sambalmueslie.openevent.core.account

import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountChangeRequest
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface AccountStorage : Storage<Long, Account, AccountChangeRequest> {
    fun findByExternalId(externalId: String): Account?
    fun findByName(name: String, pageable: Pageable): Page<Account>
    fun findByEmail(email: String): Account?
    fun createServiceAccount(request: AccountChangeRequest): Account

    fun getInfos(pageable: Pageable): Page<AccountInfo>
    fun getInfo(account: Account): AccountInfo

    fun setExternalId(id: Long, value: PatchRequest<String>): Account?
}
