package de.sambalmueslie.openevent.core.search.category

import com.jillesvangurp.searchdsls.querydsl.*
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.CategorySearchRequest
import de.sambalmueslie.openevent.core.search.common.SearchQueryBuilder
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton

@Singleton
class CategorySearchQueryBuilder : SearchQueryBuilder<CategorySearchRequest> {
    override fun buildSearchQuery(
        pageable: Pageable,
        request: CategorySearchRequest,
        actor: Account
    ): (SearchDSL.() -> Unit) = {
        from = pageable.offset.toInt()
        resultSize = pageable.size
        trackTotalHits = "true"
        query = bool {
            if (request.fullTextSearch.isNotBlank()) {
                put("minimum_should_match", 1)
                should(
                    match(CategorySearchEntryData::name, request.fullTextSearch) { boost = 2.0 },
                    matchBoolPrefix(CategorySearchEntryData::name, request.fullTextSearch)
                )
            } else {
                matchAll()
            }
        }
    }

}