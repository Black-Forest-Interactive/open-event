package de.sambalmueslie.openevent.core.search.audience

import com.jillesvangurp.searchdsls.querydsl.*
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.AudienceSearchRequest
import de.sambalmueslie.openevent.core.search.common.SearchQueryBuilder
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton

@Singleton
class AudienceSearchQueryBuilder : SearchQueryBuilder<AudienceSearchRequest> {
    override fun buildSearchQuery(
        pageable: Pageable,
        request: AudienceSearchRequest,
        actor: Account
    ): (SearchDSL.() -> Unit) = {
        from = pageable.offset.toInt()
        resultSize = pageable.size
        trackTotalHits = "true"
        query = bool {
            if (request.fullTextSearch.isNotBlank()) {
                put("minimum_should_match", 1)
                should(
                    match(AudienceSearchEntryData::name, request.fullTextSearch) { boost = 2.0 },
                    matchBoolPrefix(AudienceSearchEntryData::name, request.fullTextSearch)
                )
            } else {
                matchAll()
            }
        }
    }

}