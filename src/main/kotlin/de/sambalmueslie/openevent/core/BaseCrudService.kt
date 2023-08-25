package de.sambalmueslie.openevent.core


import de.sambalmueslie.openevent.core.model.Account
import de.sambalmueslie.openevent.forEachWithTryCatch
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

abstract class BaseCrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest, L : BusinessObjectChangeListener<T, O>>(
    private val storage: Storage<T, O, R>
) : CrudService<T, O, R, L> {

    private val listeners = mutableSetOf<L>()

    override fun register(listener: L) {
        listeners.add(listener)
    }

    override fun unregister(listener: L) {
        listeners.remove(listener)
    }

    protected fun notifyCreated(actor: Account, obj: O) {
        notify { it.handleCreated(actor, obj) }
    }

    protected fun notifyUpdated(actor: Account, obj: O) {
        notify { it.handleUpdated(actor, obj) }
    }

    protected fun notifyDeleted(actor: Account, obj: O) {
        notify { it.handleDeleted(actor, obj) }
    }

    protected fun notify(action: (L) -> Unit) {
        listeners.forEachWithTryCatch(action)
    }


    override fun get(id: T): O? {
        return storage.get(id)
    }

    override fun getAll(pageable: Pageable): Page<O> {
        return storage.getAll(pageable)
    }

    override fun create(actor: Account, request: R, properties: Map<String, Any>): O {
        val result = storage.create(request, properties)
        notifyCreated(actor, result)
        return result
    }

    override fun update(actor: Account, id: T, request: R): O {
        val result = storage.update(id, request)
        notifyUpdated(actor, result)
        return result
    }

    override fun delete(actor: Account, id: T): O? {
        val result = storage.delete(id) ?: return null
        notifyDeleted(actor, result)
        return result
    }

    override fun getByIds(ids: Set<T>): List<O> {
        return storage.getByIds(ids)
    }
}


