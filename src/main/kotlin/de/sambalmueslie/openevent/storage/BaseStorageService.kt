package de.sambalmueslie.openevent.storage


import com.github.benmanes.caffeine.cache.Caffeine
import com.github.benmanes.caffeine.cache.LoadingCache
import de.sambalmueslie.openevent.core.BusinessObject
import de.sambalmueslie.openevent.core.BusinessObjectChangeRequest
import de.sambalmueslie.openevent.core.Storage
import de.sambalmueslie.openevent.infrastructure.cache.CacheService
import de.sambalmueslie.openevent.storage.util.PageableSequence
import de.sambalmueslie.openevent.storage.util.findByIdOrNull
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger
import java.util.concurrent.TimeUnit
import kotlin.reflect.KClass

abstract class BaseStorageService<T : Any, O : BusinessObject<T>, R : BusinessObjectChangeRequest, D : DataObject>(
    private val repository: DataObjectRepository<T, D>,
    private val converter: DataObjectConverter<O, D>,

    cacheService: CacheService,
    type: KClass<O>,
    logger: Logger,
    cacheSize: Long = 100,
) : Storage<T, O, R> {

    private val cache: LoadingCache<T, O> = cacheService.register(type) {
        Caffeine.newBuilder()
            .maximumSize(cacheSize)
            .expireAfterWrite(1, TimeUnit.HOURS)
            .recordStats()
            .build { id -> repository.findByIdOrNull(id)?.let { converter.convert(it) } }
    }

    override fun get(id: T): O? {
        return cache[id]
    }

    override fun getAll(pageable: Pageable): Page<O> {
        return repository.findAll(pageable).let { converter.convert(it) }
    }

    fun getAll(): List<O> {
        return repository.findAll().let { converter.convert(it) }
    }

    fun create(request: R): O {
        return create(request, emptyMap())
    }

    override fun create(request: R, properties: Map<String, Any>): O {
        isValid(request)
        val existing = existing(request)
        if (existing != null) return converter.convert(existing)

        val result = repository.save(createData(request, properties)).let { converter.convert(it) }
        createDependencies(request, properties, result)
        cache.put(result.id, result)
        return result
    }

    protected open fun createDependencies(request: R, properties: Map<String, Any>, result: O) {
        // intentionally left empty
    }

    protected abstract fun createData(request: R, properties: Map<String, Any>): D

    override fun update(id: T, request: R): O {
        val data = repository.findByIdOrNull(id) ?: return create(request)
        isValid(request)
        val result = repository.update(updateData(data, request)).let { converter.convert(it) }
        updateDependencies(request, result)
        cache.put(result.id, result)
        return result
    }


    protected open fun updateDependencies(request: R, result: O) {
        // intentionally left empty
    }

    protected abstract fun updateData(data: D, request: R): D

    protected fun patchData(id: T, patch: (D) -> Unit): O? {
        val data = repository.findByIdOrNull(id) ?: return null
        return patchData(data, patch)
    }

    protected fun patchData(data: D, patch: (D) -> Unit): O {
        patch.invoke(data)
        val result = repository.update(data).let { converter.convert(it) }
        cache.put(result.id, result)
        return result
    }

    override fun delete(id: T): O? {
        val data = repository.findByIdOrNull(id) ?: return null
        return delete(data)
    }

    fun delete(data: D): O {
        deleteDependencies(data)
        val result = converter.convert(data)
        repository.delete(data)
        cache.invalidate(result.id)
        return result
    }

    @Deprecated("Move that to core")
    abstract fun isValid(request: R)
    protected open fun existing(request: R): D? {
        return null
    }

    protected open fun deleteDependencies(data: D) {
        // intentionally left empty
    }

    fun deleteAll() {
        val sequence = PageableSequence { repository.findAll(it) }
        sequence.forEach { delete(it) }
        cache.invalidateAll()
    }

    override fun getByIds(ids: Set<T>): List<O> {
        if (ids.isEmpty()) return emptyList()
        return repository.findByIdIn(ids).let { converter.convert(it) }
    }


}
