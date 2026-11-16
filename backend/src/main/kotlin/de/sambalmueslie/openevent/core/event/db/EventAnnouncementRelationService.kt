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
    private val repository: EventAnnouncementRelationRepository,
    private val service: AnnouncementStorageService
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventAnnouncementRelationService::class.java)
    }

    fun assign(event: Event, announcement: Announcement) {
        if (repository.existsByEventIdAndAnnouncementId(event.id,announcement.id)) return

        val relation = EventAnnouncementRelation(event.id, announcement.id)
        repository.save(relation)
    }

    fun revoke(event: Event, announcement: Announcement) {
        repository.deleteByEventIdAndAnnouncementId(event.id,announcement.id)
    }

    fun get(event: Event, pageable: Pageable): Page<Announcement> {
        val relations = repository.findByEventId(event.id, pageable)
        val categoryIds = relations.content.map { it.announcementId }.toSet()
        val result = service.getByIds(categoryIds)
        return Page.of(result, relations.pageable, relations.totalSize)
    }

    fun findEventId(announcementId: Long): Long? {
        return repository.findByAnnouncementId(announcementId)?.eventId
    }

    fun isAssigned(event: Event, announcement: Announcement): Boolean {
        return repository.findByEventIdAndAnnouncementId(event.id,announcement.id) != null
    }

    fun delete(data: EventData) {
        repository.deleteByEventId(data.id)
    }

}
