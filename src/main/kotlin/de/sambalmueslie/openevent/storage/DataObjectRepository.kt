package de.sambalmueslie.openevent.storage

import io.micronaut.data.repository.PageableRepository

interface DataObjectRepository<T, O : DataObject> : PageableRepository<O, T> {
    fun findByIdIn(ids: Set<T>): List<O>
}
