package de.sambalmueslie.openevent.common.crud


import de.sambalmueslie.openevent.common.util.findByIdOrNull
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.data.repository.PageableRepository
import org.slf4j.Logger

abstract class GenericCrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest, D : DataObject<O>>(
    private val repository: PageableRepository<D, T>,
    logger: Logger
) : BaseCrudService<T, O, R>(logger) {

    final override fun get(id: T): O? {
        return repository.findByIdOrNull(id)?.convert()
    }

    final override fun getAll(pageable: Pageable): Page<O> {
        return repository.findAll(pageable).map { it.convert() }
    }

    override fun create(request: R): O {
        isValid(request)

        val data = repository.save(createData(request)).convert()
        notifyCreated(data)
        return data
    }

    protected abstract fun createData(request: R): D

    override fun update(id: T, request: R): O {
        val data = repository.findByIdOrNull(id) ?: return create(request)
        isValid(request)
        val result = repository.update(updateData(data, request)).convert()
        notifyUpdated(result)
        return result
    }

    protected abstract fun updateData(data: D, request: R): D

    override fun delete(id: T): O? {
        val data = repository.findByIdOrNull(id) ?: return null
        repository.delete(data)
        val result = data.convert()
        notifyDeleted(result)
        return result
    }

    protected abstract fun isValid(request: R)
}


