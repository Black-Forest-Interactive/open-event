package de.sambalmueslie.openevent.storage.util


import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class PageIterator<T>(private val pageSize: Int = 500, private val provider: (pageable: Pageable) -> Page<T>) :
    Iterator<Page<T>> {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(PageIterator::class.java)
    }

    private var currentPageable = Pageable.from(0, pageSize)
    private var currentPage: Page<T>? = null

    override fun hasNext(): Boolean {
        if (hasNextPage()) {
            currentPage = provider.invoke(currentPageable)
            return true
        }
        return false
    }

    private fun hasNextPage(): Boolean {
        val p = currentPage ?: return true
        if (p.totalPages == 1) return false
        return p.pageNumber + 1 < p.totalPages
    }

    private fun loadNextPage() {
        currentPageable = currentPageable.next()
        logger.debug("Load next page $currentPageable")
        currentPage = provider.invoke(currentPageable)
    }

    override fun next(): Page<T> {
        return currentPage ?: Page.empty()
    }


}
