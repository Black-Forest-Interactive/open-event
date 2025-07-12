package de.sambalmueslie.openevent.core.share.db

import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.share.api.Share
import de.sambalmueslie.openevent.core.share.api.ShareChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class ShareStorageService(
    private val repository: ShareRepository,
    private val converter: ShareConverter,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<String, Share, ShareChangeRequest, ShareData>(repository, converter, cacheService, Share::class, logger), ShareStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(ShareStorageService::class.java)
        private const val EVENT_REFERENCE = "event"
    }

    override fun create(event: Event, request: ShareChangeRequest): Share {
        return create(request, mapOf(Pair(EVENT_REFERENCE, event)))
    }

    override fun createData(request: ShareChangeRequest, properties: Map<String, Any>): ShareData {
        val event = properties[EVENT_REFERENCE] as? Event ?: throw InvalidRequestException("Cannot find event")
        return ShareData.create(event, request, timeProvider.now())
    }

    override fun updateData(data: ShareData, request: ShareChangeRequest): ShareData {
        return data.update(request, timeProvider.now())
    }

    override fun findByEvent(event: Event): Share? {
        return repository.findByEventId(event.id)?.let { converter.convert(it) }
    }

    override fun findByEventIds(eventIds: Set<Long>): List<Share> {
        return repository.findByEventIdIn(eventIds).let { converter.convert(it) }
    }

    override fun setEnabled(id: String, value: PatchRequest<Boolean>): Share? {
        return patchData(id) { it.setPublished(value.value, timeProvider.now()) }
    }

}