package de.sambalmueslie.openevent.common.crud


import de.sambalmueslie.openevent.common.util.forEachWithTryCatch
import org.slf4j.Logger

abstract class BaseCrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest>(private val logger: Logger) : CrudService<T, O, R> {

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
}
