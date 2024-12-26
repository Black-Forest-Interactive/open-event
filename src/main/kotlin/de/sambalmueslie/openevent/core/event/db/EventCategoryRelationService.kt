package de.sambalmueslie.openevent.core.event.db


import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.db.CategoryStorageService
import de.sambalmueslie.openevent.core.event.api.Event
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class EventCategoryRelationService(
    private val repository: CategoryRelationRepository,
    private val service: CategoryStorageService
) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EventCategoryRelationService::class.java)
    }

    fun assign(event: Event, category: Category) {
        if (repository.existsByCategoryIdAndEventId(category.id, event.id)) return

        val relation = EventCategoryRelation(event.id, category.id)
        repository.save(relation)
    }

    fun revoke(event: Event, category: Category) {
        repository.deleteByCategoryIdAndEventId(category.id, event.id)
    }

    fun get(event: Event, pageable: Pageable): Page<Category> {
        val relations = repository.findByEventId(event.id, pageable)
        val categoryIds = relations.content.map { it.categoryId }.toSet()
        val result = service.getByIds(categoryIds)
        return Page.of(result, relations.pageable, relations.totalSize)
    }

    fun get(event: Event): List<Category> {
        val relations = repository.findByEventId(event.id)
        val categoryIds = relations.map { it.categoryId }.toSet()
        return service.getByIds(categoryIds)
    }

    fun getByEventIds(eventIds: Set<Long>): Map<Long, List<Category>> {
        val relations = repository.findByEventIdIn(eventIds)
        val categoryIds = relations.map { it.categoryId }.toSet()
        val categories = service.getByIds(categoryIds).associateBy { it.id }
        return relations.mapNotNull { r -> categories[r.categoryId]?.let { r.eventId to it } }
            .groupBy { it.first }
            .mapValues { it.value.map { it.second } }
    }


    fun set(event: Event, categories: List<Category>) {
        val relations = repository.findByEventId(event.id)
        val addedCategoryIds = relations.map { it.categoryId }.toSet()

        val toAdd = categories.filter { !addedCategoryIds.contains(it.id) }
            .map { EventCategoryRelation(event.id, it.id) }
        if (toAdd.isNotEmpty()) repository.saveAll(toAdd)

        val newCategoryIds = categories.map { it.id }
        val toRemove = relations.filter { !newCategoryIds.contains(it.categoryId) }
        toRemove.forEach { repository.deleteByCategoryIdAndEventId(it.categoryId, it.eventId) }
    }

    fun delete(data: EventData) {
        repository.deleteByEventId(data.id)
    }


}
