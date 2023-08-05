package de.sambalmueslie.openevent.core.logic


import de.sambalmueslie.openevent.core.BaseCrudService
import de.sambalmueslie.openevent.core.model.Category
import de.sambalmueslie.openevent.core.model.CategoryChangeRequest
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class CategoryCrudService(
    private val storage: CategoryStorage
) : BaseCrudService<Long, Category, CategoryChangeRequest>(storage, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CategoryCrudService::class.java)
    }

    fun findByName(name: String): Category? {
        return storage.findByName(name)
    }

    fun getByIds(categoryIds: Set<Long>): List<Category> {
        return storage.getByIds(categoryIds)
    }
}
