package de.sambalmueslie.openevent.core.search.event

import com.jillesvangurp.searchdsls.querydsl.*
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.EventSearchRequest
import de.sambalmueslie.openevent.core.search.common.SearchQueryBuilder
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton

@Singleton
class EventSearchQueryBuilder : SearchQueryBuilder<EventSearchRequest> {

    override fun buildSearchQuery(
        pageable: Pageable,
        request: EventSearchRequest,
        actor: Account
    ): SearchDSL.() -> Unit = {
        from = pageable.offset.toInt()
        resultSize = pageable.size
        trackTotalHits = "true"
        query = bool {
            if (request.fullTextSearch.isNotBlank()) {
                put("minimum_should_match", 1)
                should(
                    match(EventSearchEntryData::title, request.fullTextSearch) { boost = 2.0 },
                    matchBoolPrefix(EventSearchEntryData::title, request.fullTextSearch),
                    match(EventSearchEntryData::shortText, request.fullTextSearch),
                    match(EventSearchEntryData::longText, request.fullTextSearch),
                    match(EventSearchEntryData::categories, request.fullTextSearch),
                    match(EventSearchEntryData::tags, request.fullTextSearch)
                )
            }
            if (request.from != null) {
                filter(range(EventSearchEntryData::date) {
                    gte = request.from.atStartOfDay()
                })
            }
            if (request.to != null) {
                filter(range(EventSearchEntryData::date) {
                    lte = request.to.plusDays(1).atStartOfDay()
                })
            }
            if (request.ownEvents) {
                filter(term(EventSearchEntryData::owner, actor.id.toString()))
            }
            if (request.participatingEvents) {
                filter(matchPhrase(EventSearchEntryData::participant, actor.id.toString()))
            }
            if (request.onlyAvailableEvents) {
                filter(term(EventSearchEntryData::hasSpaceLeft, "true"))
            }
            agg(
                EventSearchEntryData::date.name,
                DateHistogramAgg(EventSearchEntryData::date) {
                    calendarInterval = "day"
                }
            )

        }
        sort {
            add(EventSearchEntryData::start, SortOrder.ASC)
            add(EventSearchEntryData::id, SortOrder.ASC)
        }
    }

}