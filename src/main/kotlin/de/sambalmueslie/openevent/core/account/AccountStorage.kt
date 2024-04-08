package de.sambalmueslie.openevent.core.account

import de.sambalmueslie.openevent.common.Storage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface AccountStorage : Storage<Long, Account, AccountChangeRequest> {
    fun findByExternalId(externalId: String): Account?
    fun findByName(name: String, pageable: Pageable): Page<Account>
    fun findByEmail(email: String): Account?
    fun createServiceAccount(request: AccountChangeRequest): Account
}
