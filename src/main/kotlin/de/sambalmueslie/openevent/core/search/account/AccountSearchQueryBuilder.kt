package de.sambalmueslie.openevent.core.search.account

import com.jillesvangurp.searchdsls.querydsl.*
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.AccountSearchRequest
import de.sambalmueslie.openevent.core.search.common.SearchQueryBuilder
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton

@Singleton
class AccountSearchQueryBuilder : SearchQueryBuilder<AccountSearchRequest> {
    override fun buildSearchQuery(
        pageable: Pageable,
        request: AccountSearchRequest,
        actor: Account
    ): (SearchDSL.() -> Unit) = {
        from = pageable.offset.toInt()
        resultSize = pageable.size
        trackTotalHits = "true"
        query = bool {
            if (request.fullTextSearch.isBlank()) {
                filter(matchAll())
            } else {
                filter(multiMatch(
                    request.fullTextSearch,
                    AccountSearchEntryData::name,
                    AccountSearchEntryData::email,
                    AccountSearchEntryData::mobile,
                    AccountSearchEntryData::phone,
                    AccountSearchEntryData::firstName,
                    AccountSearchEntryData::lastName
                ) {
                    type = MultiMatchType.phrase_prefix
                })
            }
        }
        sort {
            add("_score", SortOrder.DESC)
        }
    }
}