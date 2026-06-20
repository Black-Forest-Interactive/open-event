package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest
import de.sambalmueslie.openevent.core.registration.api.RegistrationChangeRequest
import java.time.LocalDateTime

/**
 * Reusable test data for events. The location is optional — pass `location = null` to create an event
 * without a location (e.g. to then exercise [de.sambalmueslie.openevent.core.location.LocationCrudService]
 * create paths directly). categoryIds / audienceIds default to empty; pass persisted ids from
 * [CategoryTestData] for richer scenarios.
 */
object EventTestData {

    fun createRequest(
        start: LocalDateTime = LocalDateTime.of(2023, 10, 1, 20, 15),
        finish: LocalDateTime = LocalDateTime.of(2023, 10, 1, 22, 15),
        title: String = "title",
        shortText: String = "short",
        longText: String = "long",
        imageUrl: String = "image",
        iconUrl: String = "icon",
        categoryIds: Set<Long> = emptySet(),
        audienceIds: Set<Long> = emptySet(),
        location: LocationChangeRequest? = LocationTestData.createRequest(),
        registration: RegistrationChangeRequest = RegistrationChangeRequest(10, true, false),
        published: Boolean = true,
        shared: Boolean = true,
        tags: Set<String> = setOf("tag"),
    ) = EventChangeRequest(
        start, finish, title, shortText, longText, imageUrl, iconUrl,
        categoryIds, audienceIds, location, registration, published, shared, tags,
    )

    fun create(
        service: EventCrudService,
        actor: Account,
        request: EventChangeRequest = createRequest(),
    ): Event = service.create(actor, request)

}
