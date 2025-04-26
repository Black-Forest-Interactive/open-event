package de.sambalmueslie.openevent.core.event.db


import de.sambalmueslie.openevent.core.announcement.api.Announcement
import de.sambalmueslie.openevent.core.announcement.db.AnnouncementStorageService
import de.sambalmueslie.openevent.core.event.api.Event
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventAnnouncementRelationService(
    private val repository: AnnouncementRelationRepository,
    private val service: AnnouncementStorageService
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventAnnouncementRelationService::class.java)
    }

    fun assign(event: Event, announcement: Announcement) {
        if (repository.existsByAnnouncementIdAndEventId(announcement.id, event.id)) return

        val relation = EventAnnouncementRelation(announcement.id, event.id)
        repository.save(relation)
    }

    fun revoke(event: Event, announcement: Announcement) {
        repository.deleteByAnnouncementIdAndEventId(announcement.id, event.id)
    }

    fun get(event: Event, pageable: Pageable): Page<Announcement> {
        val relations = repository.findByEventId(event.id, pageable)
        val categoryIds = relations.content.map { it.announcementId }.toSet()
        val result = service.getByIds(categoryIds)
        return Page.of(result, relations.pageable, relations.totalSize)
    }

    fun delete(data: EventData) {
        repository.deleteByEventId(data.id)
    }

}
