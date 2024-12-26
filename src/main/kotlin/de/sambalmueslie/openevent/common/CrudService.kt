package de.sambalmueslie.openevent.common

import de.sambalmueslie.openevent.core.account.api.Account
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable


interface CrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest, in L : BusinessObjectChangeListener<T, O>> {
    fun get(id: T): O?
    fun getByIds(ids: Set<T>): List<O>
    fun getAll(pageable: Pageable): Page<O>
    fun create(actor: Account, request: R, properties: Map<String, Any> = emptyMap()): O
    fun update(actor: Account, id: T, request: R): O
    fun delete(actor: Account, id: T): O?
    fun register(listener: L)
    fun unregister(listener: L)
}
