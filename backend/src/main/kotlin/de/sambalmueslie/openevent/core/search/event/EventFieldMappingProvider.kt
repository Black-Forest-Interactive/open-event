package de.sambalmueslie.openevent.core.search.event

import com.jillesvangurp.searchdsls.mappingdsl.FieldMappings
import de.sambalmueslie.openevent.core.search.common.FieldMappingProvider
import jakarta.inject.Singleton

@Singleton
class EventFieldMappingProvider : FieldMappingProvider {

    override fun createMappings(): FieldMappings.() -> Unit {
        return {
            number<Long>(EventSearchEntryData::id)
            date(EventSearchEntryData::created)
            date(EventSearchEntryData::updated)
            date(EventSearchEntryData::start)
            date(EventSearchEntryData::finish)
            date(EventSearchEntryData::date)
            text(EventSearchEntryData::title)
            text(EventSearchEntryData::shortText)
            text(EventSearchEntryData::longText)
            bool(EventSearchEntryData::published)
            bool(EventSearchEntryData::shared)
            number<Long>(EventSearchEntryData::owner)

            keyword(EventSearchEntryData::street)
            keyword(EventSearchEntryData::streetNumber)
            keyword(EventSearchEntryData::zip)
            keyword(EventSearchEntryData::city)
            keyword(EventSearchEntryData::country)

            geoPoint(EventSearchEntryData::geoPoint)
            number<Double>(EventSearchEntryData::lat)
            number<Double>(EventSearchEntryData::lon)

            bool(EventSearchEntryData::hasSpaceLeft)
            number<Int>(EventSearchEntryData::maxGuestAmount)
            number<Int>(EventSearchEntryData::amountAccepted)
            number<Int>(EventSearchEntryData::amountOnWaitingList)
            number<Int>(EventSearchEntryData::remainingSpace)

            number<Long>(EventSearchEntryData::participant)

            keyword(EventSearchEntryData::categories)
            keyword(EventSearchEntryData::tags)
        }
    }
}