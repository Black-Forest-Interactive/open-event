package de.sambalmueslie.openevent.common

import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class PageIteratorTest {

    private val emptyPage = Page.of(emptyList<Int>(), Pageable.from(0), 0)


    @Test()
    fun testEmptyResponse() {
        val iterator = PageIterator<Int>(100) { emptyPage }

        assertEquals(true, iterator.hasNext())

        val page = iterator.next()
        assertEquals(0, page.totalSize)

        assertEquals(false, iterator.hasNext())
    }

    @Test()
    fun testSinglePageResponse() {
        val ref1 = Page.of(listOf(1, 2, 3), Pageable.from(0), 3)
        val iterator = PageIterator<Int>(100) { ref1 }


        assertEquals(true, iterator.hasNext())

        val page = iterator.next()
        assertEquals(ref1.totalSize, page.totalSize)

        assertEquals(false, iterator.hasNext())
    }


    @Test()
    fun testMultiplePageResponse() {
        val ref1 = Page.of(listOf(1, 2, 3, 4), Pageable.from(0, 4), 7)
        val ref2 = Page.of(listOf(5, 6, 7), Pageable.from(1, 4), 7)
        val iterator = PageIterator<Int>(4) {
            when (it.number) {
                0 -> ref1
                1 -> ref2
                else -> emptyPage
            }
        }

        assertEquals(true, iterator.hasNext())

        val p1 = iterator.next()
        assertEquals(ref1.totalSize, p1.totalSize)

        assertEquals(true, iterator.hasNext())

        val p2 = iterator.next()
        assertEquals(ref2.totalSize, p2.totalSize)

        assertEquals(false, iterator.hasNext())
    }
}