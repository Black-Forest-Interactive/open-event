package de.sambalmueslie.openevent.common

import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

interface Storage<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest> {
    fun get(id: T): O?
    fun getByIds(ids: Set<T>): List<O>
    fun getAll(pageable: Pageable): Page<O>
    fun create(request: R, properties: Map<String, Any> = emptyMap()): O
    fun update(id: T, request: R): O
    fun delete(id: T): O?

}
