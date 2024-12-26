package de.sambalmueslie.openevent.common

import io.micronaut.data.repository.PageableRepository

fun <E, ID> PageableRepository<E, ID>.findByIdOrNull(id: ID): E? = this.findById(id).orElseGet { null }
fun <E : DataObject, ID> DataObjectRepository<ID, E>.findByIdOrNull(id: ID): E? = this.findById(id).orElseGet { null }
