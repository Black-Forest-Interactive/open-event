package de.sambalmueslie.openevent.user.idp


import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.user.api.User
import de.sambalmueslie.openevent.user.db.UserData
import de.sambalmueslie.openevent.user.db.UserRepository
import de.sambalmueslie.openevent.user.idp.api.IdpAPI
import de.sambalmueslie.openevent.user.idp.update.IdpUserUpdater
import io.micronaut.security.authentication.Authentication
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.Mono
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

@Singleton
class IdpUserService(
    private val api: IdpAPI,
    private val repository: UserRepository,
    private val timeProvider: TimeProvider
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(IdpUserService::class.java)
    }

    private val activeUpdater = ConcurrentHashMap<String, IdpUserUpdater>()

    private val externalIdCache: LoadingCache<String, UserData> = Caffeine.newBuilder()
        .maximumSize(10000)
        .expireAfterWrite(Duration.ofMinutes(10))
        .build { externalId -> updateData(externalId) }

    fun get(auth: Authentication): User? {
        val externalId = auth.name ?: return null
        return externalIdCache[externalId]?.convert()
    }

    private fun updateData(externalId: String): UserData? {
        if (logger.isDebugEnabled) logger.debug("Updating user data for $externalId")
        val updater = activeUpdater.getOrPut(externalId) { IdpUserUpdater(api, repository, timeProvider) }
        return updater.update(externalId)
    }
}
