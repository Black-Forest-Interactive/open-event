package de.sambalmueslie.openevent.core


import de.sambalmueslie.openevent.forEachWithTryCatch
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger

abstract class BaseCrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest>(
    private val storage: Storage<T, O, R>,
    private val logger: Logger,
) : CrudService<T, O, R> {

    private val listeners = mutableSetOf<BusinessObjectChangeListener<T, O>>()

    override fun register(listener: BusinessObjectChangeListener<T, O>) {
        listeners.add(listener)
    }

    override fun unregister(listener: BusinessObjectChangeListener<T, O>) {
        listeners.remove(listener)
    }

    protected fun notifyCreated(obj: O) {
        listeners.forEachWithTryCatch { it.handleCreated(obj) }
    }

    protected fun notifyUpdated(obj: O) {
        listeners.forEachWithTryCatch { it.handleUpdated(obj) }
    }

    protected fun notifyDeleted(obj: O) {
        listeners.forEachWithTryCatch { it.handleDeleted(obj) }
    }

    override fun get(id: T): O? {
        return storage.get(id)
    }

    override fun getAll(pageable: Pageable): Page<O> {
        return storage.getAll(pageable)
    }

    override fun create(request: R, properties: Map<String, Any>): O {
        val result = storage.create(request, properties)
        notifyCreated(result)
        return result
    }

    override fun update(id: T, request: R): O {
        val result = storage.update(id, request)
        notifyUpdated(result)
        return result
    }

    override fun delete(id: T): O? {
        val result = storage.delete(id) ?: return null
        notifyDeleted(result)
        return result
    }

    override fun getByIds(ids: Set<T>): List<O> {
        return storage.getByIds(ids)
    }
}


