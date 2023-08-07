package de.sambalmueslie.openevent.core


interface CrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest> : Storage<T, O, R> {
    fun register(listener: BusinessObjectChangeListener<T, O>)
    fun unregister(listener: BusinessObjectChangeListener<T, O>)
}
