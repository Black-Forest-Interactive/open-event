package de.sambalmueslie.openevent.common.crud

import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable


interface CrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest> {
    fun get(id: T): O?
    fun getAll(pageable: Pageable): Page<O>
    fun create(request: R): O
    fun update(id: T, request: R): O
    fun delete(id: T): O?

    fun register(listener: BusinessObjectChangeListener<T, O>)
    fun unregister(listener: BusinessObjectChangeListener<T, O>)
}
