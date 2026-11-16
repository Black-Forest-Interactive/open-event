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
                    lte = request.to.atStartOfDay().withHour(23).withMinute(59).withSecond(59)
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
            if (request.featured) {
                filter(term(EventSearchEntryData::featured, "true"))
            }
            if (request.categories.isNotEmpty()) {
                filter(terms(EventSearchEntryData::categories, request.categories))
            }
            if (request.bookmarked) {
                filter(terms(EventSearchEntryData::bookmarks, actor.id.toString()))
            }
            if (request.audiences.isNotEmpty()) {
                filter(terms(EventSearchEntryData::audiences, request.audiences))
            }
            if (request.excludedCategories.isNotEmpty()) {
                mustNot(terms(EventSearchEntryData::categories, request.excludedCategories))
            }
            if (request.excludedAudiences.isNotEmpty()) {
                mustNot(terms(EventSearchEntryData::audiences, request.excludedAudiences))
            }
            if (request.status.isNotEmpty()) {
                filter(terms(EventSearchEntryData::status, request.status.map { it.name }))
            }
            if (request.onlyPublished) {
                filter(term(EventSearchEntryData::published, "true"))
            }
            if (request.includeIds.isNotEmpty()) {
                filter(terms(EventSearchEntryData::id, request.includeIds.map { it.toString() }))
            }
            if (request.excludeIds.isNotEmpty()) {
                mustNot(terms(EventSearchEntryData::id, request.excludeIds.map { it.toString() }))
            }
            agg(
                EventSearchEntryData::date.name,
                DateHistogramAgg(EventSearchEntryData::date) {
                    calendarInterval = "day"
                }
            )
            agg(EventSearchEntryData::status.name, TermsAgg(EventSearchEntryData::status))

        }
        if (pageable.isSorted) {
            sort {
                pageable.orderBy.forEach { order ->
                    val field = if (order.property == EventSearchEntryData::title.name) {
                        EventFieldMappingProvider.TITLE_SORT_FIELD
                    } else {
                        order.property
                    }
                    add(field, if (order.isAscending) SortOrder.ASC else SortOrder.DESC)
                }
            }
        } else {
            sort {
                add(EventSearchEntryData::start, SortOrder.ASC)
                add(EventSearchEntryData::id, SortOrder.ASC)
            }
        }
    }

}