package de.sambalmueslie.openevent.core.account.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.account.AccountStorage
import de.sambalmueslie.openevent.core.account.ProfileStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.AccountChangeRequest
import de.sambalmueslie.openevent.core.account.api.AccountDetails
import de.sambalmueslie.openevent.core.account.api.AccountInfo
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AccountStorageService(
    private val repository: AccountRepository,
    private val profileStorage: ProfileStorage,
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
        private const val SERVICE_ACCOUNT = "service_account"
    }

    override fun createServiceAccount(request: AccountChangeRequest): Account {
        return create(request, mapOf(Pair(SERVICE_ACCOUNT, true)))
    }

    override fun createData(request: AccountChangeRequest, properties: Map<String, Any>): AccountData {
        val serviceAccount = properties[SERVICE_ACCOUNT] as? Boolean ?: false
        val idpLinked = !request.externalId.isNullOrBlank()
        logger.info("Create account $request")
        return AccountData.create(request, serviceAccount, idpLinked, timeProvider.now())
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

    fun getInfo(id: Long): AccountInfo? {
        val account = get(id) ?: return null
        return getInfo(account)
    }

    fun getInfoByIds(ids: Set<Long>): List<AccountInfo> {
        val accounts = repository.findByIdIn(ids)
        val profiles = profileStorage.findByIdIn(ids).associateBy { it.id }
        return accounts.map { AccountInfo.create(it, profiles[it.id]) }
    }

    override fun getInfos(pageable: Pageable): Page<AccountInfo> {
        val accounts = repository.findAll(pageable)
        val ids = accounts.map { it.id }.toSet()
        val profiles = profileStorage.findByIdIn(ids).associateBy { it.id }
        return accounts.map { AccountInfo.create(it, profiles[it.id]) }
    }

    override fun getInfo(account: Account): AccountInfo {
        val profile = profileStorage.get(account.id)
        return AccountInfo.create(account, profile)
    }

    override fun setExternalId(id: Long, value: PatchRequest<String>): Account? {
        return patchData(id) { it.setExternalId(value.value, timeProvider.now()) }
    }

    fun getDetails(id: Long): AccountDetails? {
        val account = get(id) ?: return null
        return getDetails(account)
    }

    fun getDetails(account: Account): AccountDetails {
        val profile = profileStorage.get(account.id)
        return AccountDetails.create(account, profile)
    }

    fun getDetailsByIds(ids: Set<Long>): List<AccountDetails> {
        val accounts = repository.findByIdIn(ids)
        val profiles = profileStorage.findByIdIn(ids).associateBy { it.id }
        return accounts.map { AccountDetails.create(it, profiles[it.id]) }
    }


}
