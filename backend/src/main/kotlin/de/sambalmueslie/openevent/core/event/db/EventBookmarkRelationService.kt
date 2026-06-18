package de.sambalmueslie.openevent.core.event.db


import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventBookmarkRelationService(
    private val repository: EventBookmarkRelationRepository,
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventBookmarkRelationService::class.java)
    }

    fun assign(event: Event, account: Account) {
        if (repository.existsByAccountIdAndEventId(account.id, event.id)) return

        val relation = EventBookmarkRelation(event.id, account.id)
        repository.save(relation)
    }

    fun revoke(event: Event, account: Account) {
        repository.deleteByAccountIdAndEventId(account.id, event.id)
    }

    fun get(event: Event, account: Account): EventBookmarkRelation? {
        return repository.findByAccountIdAndEventId(account.id, event.id)
    }

    fun get(account: Account, eventIds: Set<Long>): Set<Long> {
        return repository.findByAccountIdAndEventIdIn(account.id, eventIds).map { it.eventId }.toSet()
    }

    fun get(eventId: Long): List<EventBookmarkRelation> {
        return repository.findByEventId(eventId)
    }

    fun get(eventIds: Set<Long>): List<EventBookmarkRelation> {
        return repository.findByEventIdIn(eventIds)
    }

    fun delete(data: EventData) {
        repository.deleteByEventId(data.id)
    }


}
