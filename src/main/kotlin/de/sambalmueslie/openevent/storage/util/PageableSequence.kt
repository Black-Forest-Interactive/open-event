package de.sambalmueslie.openevent.storage.util


import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

class PageableSequence<T>(pageSize: Int = 100, provider: (pageable: Pageable) -> Page<T>) : Sequence<T> {

    private val iterator = PageableIterator(pageSize, provider)
    override fun iterator(): Iterator<T> {
        return iterator
    }


}
