package de.sambalmueslie.openevent.core.category


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.core.category.db.CategoryStorage
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class CategoryCrudService(
    private val storage: CategoryStorage
) : BaseCrudService<Long, Category, CategoryChangeRequest, CategoryChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CategoryCrudService::class.java)
    }

    fun findByName(name: String): Category? {
        return storage.findByName(name)
    }


}
