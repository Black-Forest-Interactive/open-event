package de.sambalmueslie.openevent.core.category.db


import de.sambalmueslie.openevent.common.BaseStorageService
import de.sambalmueslie.openevent.common.SimpleDataObjectConverter
import de.sambalmueslie.openevent.core.category.api.Category
import de.sambalmueslie.openevent.core.category.api.CategoryChangeRequest
import de.sambalmueslie.openevent.error.InvalidRequestException
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.infrastructure.time.TimeProvider
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class CategoryStorageService(
    private val repository: CategoryRepository,
    cacheService: CacheService,
    private val timeProvider: TimeProvider,
) : BaseStorageService<Long, Category, CategoryChangeRequest, CategoryData>(
    repository,
    SimpleDataObjectConverter(),
    cacheService,
    Category::class,
    logger
), CategoryStorage {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(CategoryStorageService::class.java)
    }

    override fun createData(request: CategoryChangeRequest, properties: Map<String, Any>): CategoryData {
        logger.info("Create category $request")
        return CategoryData.create(request, timeProvider.now())
    }

    override fun isValid(request: CategoryChangeRequest) {
        if (request.name.isBlank()) throw InvalidRequestException("Name cannot be blank")
    }

    override fun updateData(data: CategoryData, request: CategoryChangeRequest): CategoryData {
        return data.update(request, timeProvider.now())
    }

    override fun findByName(name: String): Category? {
        return repository.findByName(name)?.convert()
    }

    override fun getByIds(ids: Set<Long>): List<Category> {
        return repository.findByIdIn(ids).map { it.convert() }
    }

}
