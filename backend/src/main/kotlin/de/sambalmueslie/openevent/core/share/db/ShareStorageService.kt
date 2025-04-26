package de.sambalmueslie.openevent.core.share.db

import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ShareStorageService(
    private val repository: ShareRepository,
    private val converter: ShareConverter,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<String, Share, ShareChangeRequest, ShareData>(
    repository,
    converter,
    cacheService,
    Share::class,
    logger
), ShareStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ShareStorageService::class.java)
        private const val EVENT_REFERENCE = "event"
        private const val ACCOUNT_REFERENCE = "account"
    }

    override fun create(request: ShareChangeRequest, event: Event, owner: Account): Share {
        return create(request, mapOf(Pair(EVENT_REFERENCE, event), Pair(ACCOUNT_REFERENCE, owner)))
    }

    override fun createData(request: ShareChangeRequest, properties: Map<String, Any>): ShareData {
        val event = properties[EVENT_REFERENCE] as? Event ?: throw InvalidRequestException("Cannot find event")
        val owner = properties[ACCOUNT_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        return ShareData.create(owner, event, request, timeProvider.now())
    }

    override fun updateData(data: ShareData, request: ShareChangeRequest): ShareData {
        return data.update(request, timeProvider.now())
    }

    override fun isValid(request: ShareChangeRequest) {
        // intentionally left empty
    }

    override fun findByEvent(event: Event): Share? {
        return repository.findByEventId(event.id)?.let { converter.convert(it) }
    }

    override fun setPublished(id: String, value: PatchRequest<Boolean>): Share? {
        return patchData(id) { it.setPublished(value.value, timeProvider.now()) }
    }

    override fun getAllForAccount(account: Account, pageable: Pageable): Page<Share> {
        return repository.findByOwnerId(account.id, pageable).let { converter.convert(it) }
    }
}