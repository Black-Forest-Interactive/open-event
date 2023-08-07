package de.sambalmueslie.openevent.storage

import de.sambalmueslie.openevent.core.BusinessObject

interface SimpleDataObject<T : BusinessObject<*>> : DataObject {
    fun convert(): T

}
