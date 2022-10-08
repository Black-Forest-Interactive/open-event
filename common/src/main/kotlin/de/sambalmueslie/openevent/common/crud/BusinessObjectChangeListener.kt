package de.sambalmueslie.openevent.common.crud

interface BusinessObjectChangeListener<T, O : BusinessObject<T>> {
    fun handleCreated(obj: O)
    fun handleUpdated(obj: O)
    fun handleDeleted(obj: O)
}
