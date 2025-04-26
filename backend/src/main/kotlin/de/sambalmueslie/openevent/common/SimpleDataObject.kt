package de.sambalmueslie.openevent.common

interface SimpleDataObject<T : BusinessObject<*>> : DataObject {
    fun convert(): T

}
