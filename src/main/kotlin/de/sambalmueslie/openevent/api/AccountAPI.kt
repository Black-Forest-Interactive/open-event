package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.AccountChangeRequest
import de.sambalmueslie.openevent.core.model.AccountValidationResult
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface AccountAPI : CrudAPI<Long, Account, AccountChangeRequest> {
    companion object {
        const val PERMISSION_READ = "openevent.account.read"
        const val PERMISSION_WRITE = "openevent.account.write"
    }

    fun findByExternalId(auth: Authentication, externalId: String): Account?
    fun findByName(auth: Authentication, name: String, pageable: Pageable): Page<Account>
    fun findByEmail(auth: Authentication, email: String): Account?
    fun validate(auth: Authentication): AccountValidationResult
}
