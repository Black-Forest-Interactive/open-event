package de.sambalmueslie.openevent.storage.util


import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable

class PageSequence<T>(pageSize: Int = 100, provider: (pageable: Pageable) -> Page<T>) : Sequence<Page<T>> {

    private val iterator = PageIterator(pageSize, provider)
    override fun iterator(): Iterator<Page<T>> {
        return iterator
    }


}
