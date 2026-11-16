package de.sambalmueslie.openevent.core.finder

import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.EventStatus
import de.sambalmueslie.openevent.core.finder.api.*
import de.sambalmueslie.openevent.core.finder.db.FinderStorage
import de.sambalmueslie.openevent.core.search.SearchService
import de.sambalmueslie.openevent.core.search.api.EventSearchEntry
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class FinderCrudService(
    private val storage: FinderStorage,
    private val eventService: EventCrudService,
    private val searchService: SearchService,
) : BaseCrudService<Long, Finder, FinderChangeRequest, FinderChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(FinderCrudService::class.java)
    }

    fun create(actor: Account, request: FinderChangeRequest): Finder {
        isValid(request)
        val result = storage.create(request, actor)
        notifyCreated(actor, result)
        return result
    }

    override fun isValid(request: FinderChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Name cannot be blank")
        val from = request.request.from ?: throw InvalidRequestException("From date must be set")
        val to = request.request.to ?: throw InvalidRequestException("To date must be set")
        if (to.isBefore(from)) throw InvalidRequestException("To date must not be before from date")
    }

    fun getForAccount(account: Account, pageable: Pageable): Page<Finder> {
        return storage.findByOwner(account, pageable)
    }

    fun getInfos(actor: Account, pageable: Pageable): Page<FinderInfo> {
        return getForAccount(actor, pageable).map { getInfo(actor, it) }
    }

    fun getInfo(actor: Account, finder: Finder): FinderInfo {
        val entries = storage.getEntries(finder)
        val answered = entries.map { it.eventId }.toSet()
        val byAnswer = entries.groupBy({ it.answer }, { it.eventId })
        return FinderInfo(
            finder.id,
            finder.name,
            finder.request,
            finder.timestamp,
            count(actor, finder, exclude = answered),
            countAnswer(actor, finder, byAnswer[FinderAnswer.YES]),
            countAnswer(actor, finder, byAnswer[FinderAnswer.NO]),
            countAnswer(actor, finder, byAnswer[FinderAnswer.MAYBE]),
        )
    }

    fun getOpen(actor: Account, finder: Finder, pageable: Pageable): Page<EventSearchEntry> {
        val answered = storage.getEntries(finder).map { it.eventId }.toSet()
        return search(actor, finder, pageable, exclude = answered)
    }

    fun preview(actor: Account, request: FinderChangeRequest): FinderPreview {
        val total = search(actor, request.request, Pageable.from(0, 1)).totalSize
        return FinderPreview(total)
    }

    fun getEntries(actor: Account, finder: Finder, answer: FinderAnswer, pageable: Pageable): Page<FinderEntryInfo> {
        val entries = storage.getEntries(finder).filter { it.answer == answer }.associateBy { it.eventId }
        if (entries.isEmpty()) return Page.empty()
        val result = search(actor, finder, pageable, include = entries.keys)
        return result.map { e ->
            val entry = entries[e.id.toLong()]
            FinderEntryInfo(e, answer, entry?.timestamp ?: finder.timestamp)
        }
    }

    fun setAnswer(actor: Account, finder: Finder, eventId: Long, answer: FinderAnswer): FinderEntry? {
        val event = eventService.get(eventId) ?: return null
        if (!event.published) throw InvalidRequestException("Cannot answer unpublished event")
        return storage.updateEntry(finder, event, answer)
    }

    fun clearAnswer(actor: Account, finder: Finder, eventId: Long): Finder? {
        val event = eventService.get(eventId) ?: return null
        storage.deleteEntry(finder, event)
        return finder
    }

    fun resetAnswers(actor: Account, finder: Finder): Finder {
        storage.deleteEntries(finder)
        return finder
    }

    private fun countAnswer(actor: Account, finder: Finder, eventIds: List<Long>?): Long {
        if (eventIds.isNullOrEmpty()) return 0
        return count(actor, finder, include = eventIds.toSet())
    }

    private fun count(actor: Account, finder: Finder, include: Set<Long> = emptySet(), exclude: Set<Long> = emptySet()): Long {
        return search(actor, finder, Pageable.from(0, 1), include, exclude).totalSize
    }

    private fun search(
        actor: Account,
        finder: Finder,
        pageable: Pageable,
        include: Set<Long> = emptySet(),
        exclude: Set<Long> = emptySet()
    ): Page<EventSearchEntry> = search(actor, finder.request, pageable, include, exclude)

    private fun search(
        actor: Account,
        base: EventSearchRequest,
        pageable: Pageable,
        include: Set<Long> = emptySet(),
        exclude: Set<Long> = emptySet()
    ): Page<EventSearchEntry> {
        // closed events (canceled, ended) automatically drop out of the result by restricting to active ones
        val request = base.copy(
            ownEvents = false,
            bookmarked = false,
            participatingEvents = false,
            status = setOf(EventStatus.ACTIVE),
            onlyPublished = true,
            includeIds = include,
            excludeIds = exclude,
        )
        return searchService.searchEvents(actor, request, pageable).result
    }

}
