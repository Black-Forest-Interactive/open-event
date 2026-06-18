package de.sambalmueslie.openevent.core.event.db


import de.sambalmueslie.openevent.core.audience.api.Audience
import de.sambalmueslie.openevent.core.audience.db.AudienceStorageService
import de.sambalmueslie.openevent.core.event.api.Event
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventAudienceRelationService(
    private val repository: EventAudienceRelationRepository,
    private val service: AudienceStorageService
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventAudienceRelationService::class.java)
    }

    fun assign(event: Event, audience: Audience) {
        if (repository.existsByAudienceIdAndEventId(audience.id, event.id)) return

        val relation = EventAudienceRelation(event.id, audience.id)
        repository.save(relation)
    }

    fun revoke(event: Event, audience: Audience) {
        repository.deleteByAudienceIdAndEventId(audience.id, event.id)
    }

    fun get(event: Event, pageable: Pageable): Page<Audience> {
        val relations = repository.findByEventId(event.id, pageable)
        val audienceIds = relations.content.map { it.audienceId }.toSet()
        val result = service.getByIds(audienceIds)
        return Page.of(result, relations.pageable, relations.totalSize)
    }

    fun get(event: Event): List<Audience> {
        val relations = repository.findByEventId(event.id)
        val audienceIds = relations.map { it.audienceId }.toSet()
        return service.getByIds(audienceIds)
    }

    fun getByEventIds(eventIds: Set<Long>): Map<Long, List<Audience>> {
        val relations = repository.findByEventIdIn(eventIds)
        val audienceIds = relations.map { it.audienceId }.toSet()
        val audiences = service.getByIds(audienceIds).associateBy { it.id }
        return relations.mapNotNull { r -> audiences[r.audienceId]?.let { r.eventId to it } }
            .groupBy { it.first }
            .mapValues { it.value.map { it.second } }
    }


    fun set(event: Event, audiences: List<Audience>) {
        val relations = repository.findByEventId(event.id)
        val addedAudienceIds = relations.map { it.audienceId }.toSet()

        val toAdd = audiences.filter { !addedAudienceIds.contains(it.id) }
            .map { EventAudienceRelation(event.id, it.id) }
        if (toAdd.isNotEmpty()) repository.saveAll(toAdd)

        val newAudienceIds = audiences.map { it.id }
        val toRemove = relations.filter { !newAudienceIds.contains(it.audienceId) }
        toRemove.forEach { repository.deleteByAudienceIdAndEventId(it.audienceId, it.eventId) }
    }

    fun delete(data: EventData) {
        repository.deleteByEventId(data.id)
    }


}
