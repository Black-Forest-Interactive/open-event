package de.sambalmueslie.openevent.core.search.event

import com.jillesvangurp.searchdsls.mappingdsl.FieldMappings
import de.sambalmueslie.openevent.core.search.common.FieldMappingProvider
import jakarta.inject.Singleton

@Singleton
class EventFieldMappingProvider : FieldMappingProvider {

    override fun createMappings(): FieldMappings.() -> Unit {
        return {
            number<Long>("id")
            date("start")
            date("finish")
            date("date")
            text("title")
            text("shortText")
            text("longText")
            bool("published")
            number<Long>("owner")

            keyword("street")
            keyword("streetNumber")
            keyword("zip")
            keyword("city")
            keyword("country")

            geoPoint("geo")

            bool("hasSpaceLeft")
            number<Int>("maxGuestAmount")
            number<Int>("amountAccepted")
            number<Int>("amountOnWaitingList")
            number<Int>("remainingSpace")

            number<Long>("participant")

            keyword("categories")
            keyword("tags")
        }
    }
}