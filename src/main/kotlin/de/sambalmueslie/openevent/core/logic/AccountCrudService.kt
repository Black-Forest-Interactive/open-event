package de.sambalmueslie.openevent.core.logic


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.auth.*
import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.AccountChangeRequest
import de.sambalmueslie.openevent.core.model.AccountValidationResult
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AccountCrudService(
    private val storage: AccountStorage
) : BaseCrudService<Long, Account, AccountChangeRequest>(storage, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountCrudService::class.java)
    }

    fun findByExternalId(externalId: String): Account? {
        return storage.findByExternalId(externalId)
    }

    fun findByName(name: String, pageable: Pageable): Page<Account> {
        return storage.findByName(name, pageable)
    }

    fun findByEmail(email: String): Account? {
        return storage.findByEmail(email)
    }

    fun validate(auth: Authentication): AccountValidationResult {
        val email = auth.getEmail()
        val existing = storage.findByEmail(email)
        if (existing != null) return AccountValidationResult(false, existing)

        val result = storage.create(AccountChangeRequest(auth.getUsername(), auth.getFirstName(), auth.getLastName(), email, "", auth.getExternalId()))
        return AccountValidationResult(true, result)
    }
}
