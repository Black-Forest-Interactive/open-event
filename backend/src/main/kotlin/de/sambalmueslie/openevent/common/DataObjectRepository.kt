package de.sambalmueslie.openevent.common

import io.micronaut.data.repository.PageableRepository

interface DataObjectRepository<T: Any, O : DataObject> : PageableRepository<O, T> {
    fun findByIdIn(ids: Set<T>): List<O>
}
