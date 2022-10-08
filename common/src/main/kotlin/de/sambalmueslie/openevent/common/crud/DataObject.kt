package de.sambalmueslie.openevent.common.crud

interface DataObject<T : BusinessObject<*>> {
	fun convert(): T
}
