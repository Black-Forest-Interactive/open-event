package de.sambalmueslie.openevent.core.search.event

import com.jillesvangurp.searchdsls.querydsl.*
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.search.api.EventCreatedSearchRequest
import de.sambalmueslie.openevent.core.search.common.SearchQueryBuilder
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton

@Singleton
class EventCreatedSearchQueryBuilder : SearchQueryBuilder<EventCreatedSearchRequest> {
    override fun buildSearchQuery(pageable: Pageable, request: EventCreatedSearchRequest, actor: Account): SearchDSL.() -> Unit = {
        from = pageable.offset.toInt()
        resultSize = pageable.size
        trackTotalHits = "true"
        query = bool {
            if (request.from != null) {
                filter(range(EventSearchEntryData::updated) {
                    gte = request.from
                })
            }
            if (request.to != null) {
                filter(range(EventSearchEntryData::updated) {
                    lte = request.to
                })
            }
            if (request.onlyPublishedEvents) {
                filter(term(EventSearchEntryData::published, "true"))
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
        }
    }
}