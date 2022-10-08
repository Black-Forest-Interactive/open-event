package de.sambalmueslie.openevent.user.idp.update


import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.common.util.executeWithReturn
import de.sambalmueslie.openevent.common.util.measureTimeMillisWithReturn
import de.sambalmueslie.openevent.user.db.UserData
import de.sambalmueslie.openevent.user.db.UserRepository
import de.sambalmueslie.openevent.user.idp.api.IdpAPI
import org.keycloak.representations.idm.UserRepresentation
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.concurrent.Semaphore

class IdpUserUpdater(
    private val api: IdpAPI,
    private val repository: UserRepository,
    private val timeProvider: TimeProvider
) {


    companion object {
        private val logger: Logger = LoggerFactory.getLogger(IdpUserUpdater::class.java)
    }

    private val semaphore = Semaphore(1)

    fun update(externalId: String): UserData? {
        if (logger.isDebugEnabled) logger.debug("[$externalId] Update user - start")
        val (duration, result) = measureTimeMillisWithReturn {
            try {
                logger.isTraceEnabled.let { logger.trace("[$externalId] Semaphore acquire") }
                semaphore.acquire()
                updateUser(externalId)
            } catch (e: Exception) {
                logger.error("[$externalId] Update user - fail", e)
                null
            } finally {
                semaphore.release()
                logger.isTraceEnabled.let { logger.trace("[$externalId] Semaphore release") }
            }
        }
        logger.debug("[$externalId] Update user - $externalId finished within $duration ms")
        return result
    }

    private fun updateUser(externalId: String): UserData? {
        val data = repository.findByExternalId(externalId).firstOrNull()
        return if (data != null) checkExisting(data) else syncWithIdp(externalId)
    }

    private fun syncWithIdp(externalId: String): UserData? {
        if (logger.isDebugEnabled) logger.debug("[$externalId] Update user - sync with idp")
        val idpValue = api.getUserRepresentation(externalId) ?: return executeWithReturn { logger.error("Cannot find idp user for $externalId") }
        val existing = repository.findByExternalId(idpValue.id).firstOrNull() ?: idpValue.email?.let { repository.findByEmail(idpValue.email).firstOrNull { it.externalId.isBlank() } }

        val value = if (existing != null) {
            sync(idpValue, existing)
        } else {
            convert(idpValue)
        }
        if (logger.isTraceEnabled) logger.trace("[$externalId] Update user - store data $value")
        return repository.save(value)
    }

    private fun convert(representation: UserRepresentation): UserData {
        if (logger.isDebugEnabled) logger.debug("[${representation.id}] Update user - convert from representation")
        return UserData(
            0,
            representation.id,
            representation.username ?: "",
            representation.firstName ?: "",
            representation.lastName ?: "",
            representation.email ?: "",
            timeProvider.now(),
            null
        )
    }

    private fun checkExisting(data: UserData): UserData? {
        if (logger.isDebugEnabled) logger.debug("[${data.externalId}] Update user - check existing")
        if (isSyncRequired(data)) {
            return syncWithIdp(data)
        }
        return data
    }

    private fun isSyncRequired(data: UserData): Boolean {
        val lastSync = data.updated ?: data.created
        return lastSync.isBefore(timeProvider.now().minusDays(1))
    }

    private fun syncWithIdp(data: UserData): UserData? {
        val idpValue = api.getUserRepresentation(data.externalId) ?: return executeWithReturn { logger.error("Cannot find idp user for ${data.externalId}") }
        return repository.save(sync(idpValue, data))
    }

    private fun sync(representation: UserRepresentation, user: UserData): UserData {
        if (logger.isDebugEnabled) logger.debug("[${user.externalId}] Update user - sync with representation")
        user.externalId = representation.id
        user.userName = representation.username ?: ""
        user.firstName = representation.firstName ?: ""
        user.lastName = representation.lastName ?: ""
        user.email = representation.email ?: ""
        user.updated = timeProvider.now()
        return user
    }
}
