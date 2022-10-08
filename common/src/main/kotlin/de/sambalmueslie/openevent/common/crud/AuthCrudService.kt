package de.sambalmueslie.openevent.common.crud

import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication
import reactor.core.publisher.Mono

interface AuthCrudService<T, O : BusinessObject<T>, R : BusinessObjectChangeRequest> {
    fun get(auth: Authentication, id: T): Mono<O>
    fun getAll(auth: Authentication, pageable: Pageable): Mono<Page<O>>
    fun create(auth: Authentication, request: R): Mono<O>
    fun update(auth: Authentication, id: T, request: R): Mono<O>
    fun delete(auth: Authentication, id: T): Mono<Long>
}
