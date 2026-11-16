package de.sambalmueslie.openevent.core.search

import com.jillesvangurp.jsondsl.json
import com.jillesvangurp.searchdsls.querydsl.SearchDSL
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.event.EventSearchQueryBuilder
import io.micronaut.data.model.Pageable
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class EventSearchQueryBuilderTest {

    private val builder = EventSearchQueryBuilder()
    private val actor = mockk<Account> { every { id } returns 42 }
    private val request = EventSearchRequest("", null, null, false, false, false, false, false)

    private fun json(request: EventSearchRequest): String {
        val dsl = SearchDSL()
        builder.buildSearchQuery(Pageable.from(0, 10), request, actor).invoke(dsl)
        return dsl.json(false).replace(Regex("\\s"), "")
    }

    @Test
    fun restrictsToPublishedOrOwnEvents() {
        val query = json(request.copy(restrictToVisible = true))
        assertTrue(query.contains("\"published\":{\"value\":\"true\"}"), query)
        assertTrue(query.contains("\"owner\":{\"value\":\"42\"}"), query)
        assertTrue(query.contains("\"minimum_should_match\":1"), query)
    }

    @Test
    fun doesNotRestrictByDefault() {
        val query = json(request)
        assertFalse(query.contains("\"owner\""), query)
        assertFalse(query.contains("\"published\""), query)
    }
}
