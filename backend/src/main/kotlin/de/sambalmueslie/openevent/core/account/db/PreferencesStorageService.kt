package de.sambalmueslie.openevent.core.account.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.core.account.PreferencesStorage
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.account.api.Preferences
import de.sambalmueslie.openevent.core.account.api.PreferencesChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class PreferencesStorageService(
    private val repository: PreferencesRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Preferences, PreferencesChangeRequest, PreferencesData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Preferences::class,
    logger
), PreferencesStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PreferencesStorageService::class.java)
        private const val ACCOUNT_REFERENCE = "account"
    }

    override fun create(request: PreferencesChangeRequest, account: Account): Preferences {
        return create(request, mapOf(Pair(ACCOUNT_REFERENCE, account)))
    }

    override fun createData(request: PreferencesChangeRequest, properties: Map<String, Any>): PreferencesData {
        val account = properties[ACCOUNT_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        return PreferencesData.create(account, request, timeProvider.now())
    }

    override fun updateData(data: PreferencesData, request: PreferencesChangeRequest): PreferencesData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: PreferencesChangeRequest) {
        // intentionally left empty
    }


    override fun findByAccount(account: Account): Preferences? {
        return repository.findByIdOrNull(account.id)?.convert()
    }

}
