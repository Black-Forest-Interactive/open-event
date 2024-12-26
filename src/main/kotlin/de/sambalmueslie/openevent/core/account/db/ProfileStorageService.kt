package de.sambalmueslie.openevent.core.account.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.core.account.ProfileStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Profile
import de.sambalmueslie.openevent.core.account.api.ProfileChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ProfileStorageService(
    private val repository: ProfileRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Profile, ProfileChangeRequest, ProfileData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Profile::class,
    logger
), ProfileStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ProfileStorageService::class.java)
        private const val ACCOUNT_REFERENCE = "account"
    }

    override fun create(request: ProfileChangeRequest, account: Account): Profile {
        return create(request, mapOf(Pair(ACCOUNT_REFERENCE, account)))
    }

    override fun createData(request: ProfileChangeRequest, properties: Map<String, Any>): ProfileData {
        val account = properties[ACCOUNT_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        return ProfileData.create(account, request, timeProvider.now())
    }

    override fun updateData(data: ProfileData, request: ProfileChangeRequest): ProfileData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: ProfileChangeRequest) {
        // intentionally left empty
    }

    override fun findByAccount(account: Account): Profile? {
        return repository.findByIdOrNull(account.id)?.convert()
    }

    override fun getForAccounts(accounts: Collection<Account>): List<Profile> {
        val ids = accounts.map { it.id }.toSet()
        return findByIdIn(ids)
    }

    override fun findByIdIn(ids: Set<Long>): List<Profile> {
        return repository.findByIdIn(ids).map { it.convert() }
    }


}
