package de.sambalmueslie.openevent.core.link.db

import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.PatchRequest
import de.sambalmueslie.openevent.core.link.api.Link
import de.sambalmueslie.openevent.core.link.api.LinkChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class LinkStorageService(
    private val repository: LinkRepository,
    private val converter: LinkConverter,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<String, Link, LinkChangeRequest, LinkData>(repository, converter, cacheService, Link::class, logger), LinkStorage {
    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LinkStorageService::class.java)
        private const val KEY_REFERENCE = "key"
    }

    override fun create(key: String, request: LinkChangeRequest): Link {
        return create(request, mapOf(Pair(KEY_REFERENCE, key)))
    }

    override fun createData(request: LinkChangeRequest, properties: Map<String, Any>): LinkData {
        val key = properties[KEY_REFERENCE] as? String ?: throw InvalidRequestException("Cannot find key")
        return LinkData.create(key, request, timeProvider.now())
    }

    override fun updateData(data: LinkData, request: LinkChangeRequest): LinkData {
        return data.update(request, timeProvider.now())
    }

    override fun findByKeys(keys: Set<String>): List<Link> {
        return repository.findByKeyIn(keys).let { converter.convert(it) }
    }

    override fun findByKey(key: String): Link? {
        return repository.findByKey(key)?.let { converter.convert(it) }
    }

    override fun setEnabled(id: String, value: PatchRequest<Boolean>): Link? {
        return patchData(id) { it.setPublished(value.value, timeProvider.now()) }
    }

}