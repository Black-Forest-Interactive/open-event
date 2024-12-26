package de.sambalmueslie.openevent.common


import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PageableIterator<T>(private val pageSize: Int = 500, private val provider: (pageable: Pageable) -> Page<T>) :
    Iterator<T> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PageableIterator::class.java)
    }

    private var currentPageable = Pageable.from(0, pageSize)
    private var currentPage: Page<T> = provider.invoke(currentPageable)
    private var currentPageIterator = currentPage.iterator()

    override fun hasNext(): Boolean {
        if (currentPageIterator.hasNext()) return true

        if (hasNextPage()) {
            loadNextPage()
            return currentPageIterator.hasNext()
        }
        return false
    }

    private fun hasNextPage(): Boolean {
        if (currentPage.isEmpty) return false
        if (currentPage.totalPages == 1) return false
        return currentPage.pageNumber + 1 < currentPage.totalPages
    }

    private fun loadNextPage() {
        currentPageable = currentPageable.next()
        logger.debug("Load next page $currentPageable")
        currentPage = provider.invoke(currentPageable)
        currentPageIterator = currentPage.iterator()
    }

    override fun next(): T {
        return currentPageIterator.next()
    }


}
