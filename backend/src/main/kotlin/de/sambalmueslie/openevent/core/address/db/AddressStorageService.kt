package de.sambalmueslie.openevent.core.address.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AddressStorageService(
    private val repository: AddressRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Address, AddressChangeRequest, AddressData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Address::class,
    logger
), AddressStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AddressStorageService::class.java)
        private const val ACCOUNT_REFERENCE = "account"
    }

    override fun create(request: AddressChangeRequest, account: Account): Address {
        return create(request, mapOf(Pair(ACCOUNT_REFERENCE, account)))
    }

    override fun createData(request: AddressChangeRequest, properties: Map<String, Any>): AddressData {
        val account = properties[ACCOUNT_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        return AddressData.create(account, request, timeProvider.now())
    }

    override fun updateData(data: AddressData, request: AddressChangeRequest): AddressData {
        return data.update(request, timeProvider.now())
    }

    override fun findByAccount(account: Account, pageable: Pageable): Page<Address> {
        return repository.findByAccountId(account.id, pageable).map { it.convert() }
    }

    override fun getData(id: Long): AddressData? {
        return repository.findByIdOrNull(id)
    }

}
