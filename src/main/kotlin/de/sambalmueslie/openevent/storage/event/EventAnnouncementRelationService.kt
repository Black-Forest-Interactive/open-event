package de.sambalmueslie.openevent.storage.event


import de.sambalmueslie.openevent.core.model.Announcement
import de.sambalmueslie.openevent.core.model.Event
import de.sambalmueslie.openevent.storage.announcement.AnnouncementStorageService
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
        val result = service.findByIds(categoryIds)
        return Page.of(result, relations.pageable, relations.totalSize)
    }

}
