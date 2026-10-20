package de.sambalmueslie.openevent.core.finder.db

import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.findByIdOrNull
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.finder.api.Finder
import de.sambalmueslie.openevent.core.finder.api.FinderAnswer
import de.sambalmueslie.openevent.core.finder.api.FinderChangeRequest
import de.sambalmueslie.openevent.core.finder.api.FinderEntry
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class FinderStorageService(
    private val repository: FinderRepository,
    private val entryRepository: FinderEntryRepository,
    private val converter: FinderConverter,

    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Finder, FinderChangeRequest, FinderData>(
    repository,
    converter,
    cacheService,
    Finder::class,
    logger
), FinderStorage {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FinderStorageService::class.java)
        private const val OWNER_REFERENCE = "owner"
    }

    override fun create(request: FinderChangeRequest, owner: Account): Finder {
        return create(request, mapOf(OWNER_REFERENCE to owner))
    }

    override fun createData(request: FinderChangeRequest, properties: Map<String, Any>): FinderData {
        val owner = properties[OWNER_REFERENCE] as? Account ?: throw InvalidRequestException("Cannot find account")
        return FinderData.create(owner, request, timeProvider.now())
    }

    override fun updateData(data: FinderData, request: FinderChangeRequest): FinderData {
        return data.update(request, timeProvider.now())
    }


    override fun updateEntry(finder: Finder, event: Event, answer: FinderAnswer): FinderEntry {
        val id = FinderEntryId(finder.id, event.id)
        val existing = entryRepository.findByIdOrNull(id)
        return if (existing != null) {
            entryRepository.update(existing.update(answer, timeProvider.now()))
        } else {
            entryRepository.save(FinderEntryData.create(id, answer, timeProvider.now()))
        }.convert()
    }

    override fun deleteEntry(finder: Finder, event: Event) {
        val id = FinderEntryId(finder.id, event.id)
        entryRepository.deleteById(id)
    }

    override fun deleteDependencies(data: FinderData) {
        entryRepository.deleteByIdFinderId(data.id)
    }

    override fun deleteEntries(finder: Finder) {
        entryRepository.deleteByIdFinderId(finder.id)
    }

    override fun getEntries(finder: Finder): List<FinderEntry> {
        return entryRepository.findByIdFinderId(finder.id).map { it.convert() }
    }

    override fun deleteByEvent(event: Event) {
        entryRepository.deleteByIdEventId(event.id)
    }

    override fun findByOwner(owner: Account, pageable: Pageable): Page<Finder> {
        return converter.convert(repository.findByOwnerId(owner.id, pageable))
    }

}