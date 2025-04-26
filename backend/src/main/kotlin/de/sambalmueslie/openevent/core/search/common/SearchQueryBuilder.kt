package de.sambalmueslie.openevent.core.search.common

import com.jillesvangurp.searchdsls.querydsl.SearchDSL
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.SearchRequest
import io.micronaut.data.model.Pageable

interface SearchQueryBuilder<R : SearchRequest> {
    fun buildSearchQuery(pageable: Pageable, request: R, actor: Account): SearchDSL.() -> Unit
}