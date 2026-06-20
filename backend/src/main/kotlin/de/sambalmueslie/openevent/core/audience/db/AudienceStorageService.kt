package de.sambalmueslie.openevent.core.audience.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.audience.api.AudienceChangeRequest
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AudienceStorageService(
    private val repository: AudienceRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Audience, AudienceChangeRequest, AudienceData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Audience::class,
    logger
), AudienceStorage {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AudienceStorageService::class.java)
    }

    override fun createData(request: AudienceChangeRequest, properties: Map<String, Any>): AudienceData {
        logger.info("Create audience $request")
        return AudienceData.create(request, timeProvider.now())
    }

    override fun updateData(data: AudienceData, request: AudienceChangeRequest): AudienceData {
        return data.update(request, timeProvider.now())
    }

    override fun findByName(name: String): Audience? {
        return repository.findByName(name)?.convert()
    }

    override fun getByIds(ids: Set<Long>): List<Audience> {
        return repository.findByIdIn(ids).map { it.convert() }
    }

}
