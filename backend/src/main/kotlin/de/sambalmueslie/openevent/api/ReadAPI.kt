package de.sambalmueslie.openevent.api

import de.sambalmueslie.openevent.common.BusinessObject
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.security.authentication.Authentication

interface ReadAPI<T, O : BusinessObject<T>> {
    fun get(auth: Authentication, id: T): O?
    fun getAll(auth: Authentication, pageable: Pageable): Page<O>
}
