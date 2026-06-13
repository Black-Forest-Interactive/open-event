package de.sambalmueslie.openevent.common

import io.micronaut.data.repository.PageableRepository

fun <E: Any, ID: Any> PageableRepository<E, ID>.findByIdOrNull(id: ID): E? = this.findById(id).orElseGet { null }
fun <E : DataObject, ID: Any> DataObjectRepository<ID, E>.findByIdOrNull(id: ID): E? = this.findById(id).orElseGet { null }
