package de.sambalmueslie.openevent.storage.account


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.core.model.AccountChangeRequest
import de.sambalmueslie.openevent.core.storage.AccountStorage
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import de.sambalmueslie.openevent.storage.BaseStorageService
import de.sambalmueslie.openevent.storage.SimpleDataObjectConverter
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AccountStorageService(
    private val repository: AccountRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Account, AccountChangeRequest, AccountData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Account::class,
    logger
), AccountStorage {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AccountStorageService::class.java)
    }

    override fun createData(request: AccountChangeRequest, properties: Map<String, Any>): AccountData {
        logger.info("Create world $request")
        return AccountData.create(request, timeProvider.now())
    }

    override fun isValid(request: AccountChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Name cannot be blank")
    }

    override fun updateData(data: AccountData, request: AccountChangeRequest): AccountData {
        return data.update(request, timeProvider.now())
    }

    override fun findByExternalId(externalId: String): Account? {
        return repository.findByExternalId(externalId)?.convert()
    }

    override fun findByName(name: String, pageable: Pageable): Page<Account> {
        return repository.findByName(name, pageable).map { it.convert() }
    }

    override fun findByEmail(email: String): Account? {
        return repository.findByEmail(email)?.convert()
    }

    fun getByIds(ids: Set<Long>): List<Account> {
        return repository.findByIdIn(ids).map { it.convert() }
    }


}
