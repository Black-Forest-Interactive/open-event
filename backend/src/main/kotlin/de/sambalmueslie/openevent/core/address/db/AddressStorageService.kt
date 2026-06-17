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
        private const val DEFAULT_REFERENCE = "default"
    }

    override fun create(request: AddressChangeRequest, default: Boolean, account: Account): Address {
        return create(
            request, mapOf(
                ACCOUNT_REFERENCE to account,
                DEFAULT_REFERENCE to default
            )
        )
    }

    override fun createData(request: AddressChangeRequest, properties: Map<String, Any>): AddressData {
        val account = properties[ACCOUNT_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        val default = properties[DEFAULT_REFERENCE] as? Boolean ?: false
        return AddressData.create(account, request, default, timeProvider.now())
    }

    override fun updateData(data: AddressData, request: AddressChangeRequest): AddressData {
        return data.update(request, timeProvider.now())
    }

    override fun findByAccount(account: Account, pageable: Pageable): Page<Address> {
        return repository.findByAccountId(account.id, pageable).map { it.convert() }
    }

    override fun getDefault(account: Account): Address? {
        return repository.findByAccountIdAndDefaultTrue(account.id)?.convert()
    }

    override fun setDefault(id: Long, default: Boolean): Address? {
        return patchData(id) { it.setDefault(default, timeProvider.now()) }
    }

    override fun existsByAccount(account: Account): Boolean {
        return repository.existsByAccountId(account.id)
    }

    override fun getData(id: Long): AddressData? {
        return repository.findByIdOrNull(id)
    }

}
