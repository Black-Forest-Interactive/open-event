package de.sambalmueslie.openevent.storage.util

import io.micronaut.data.repository.PageableRepository

fun <E, ID> PageableRepository<E, ID>.findByIdOrNull(id: ID): E? = this.findById(id).orElseGet { null }
