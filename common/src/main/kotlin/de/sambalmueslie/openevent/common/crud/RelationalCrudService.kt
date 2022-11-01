package de.sambalmueslie.openevent.common.crud


import de.sambalmueslie.openevent.common.util.findByIdOrNull
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.PageableRepository
import org.slf4j.Logger

abstract class RelationalCrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest, D : RelationalDataObject<O>>(
    private val repository: PageableRepository<D, T>,
    logger: Logger
) : BaseCrudService<T, O, R>(logger) {

    final override fun get(id: T): O? {
        return repository.findByIdOrNull(id)?.let { convert(it) }
    }

    final override fun getAll(pageable: Pageable): Page<O> {
        return repository.findAll(pageable).map { convert(it) }
    }

    override fun create(request: R): O {
        isValid(request)

        val data = repository.save(createData(request))
        createRelationalData(request, data)
        val result = convert(data)
        notifyCreated(result)
        return result
    }

    protected abstract fun convert(data: D): O

    protected abstract fun createData(request: R): D
    protected abstract fun createRelationalData(request: R, data: D)

    override fun update(id: T, request: R): O {
        var data = repository.findByIdOrNull(id) ?: return create(request)
        isValid(request)
        data = repository.update(updateData(data, request))
        updateRelationalData(request, data)
        val result = convert(data)
        notifyUpdated(result)
        return result
    }

    protected abstract fun updateData(data: D, request: R): D
    protected abstract fun updateRelationalData(request: R, data: D)

    override fun delete(id: T): O? {
        val data = repository.findByIdOrNull(id) ?: return null
        deleteRelationalData(data)
        repository.delete(data)
        val result = convert(data)
        notifyDeleted(result)
        return result
    }

    protected abstract fun deleteRelationalData(data: D)

    protected abstract fun isValid(request: R)
}


