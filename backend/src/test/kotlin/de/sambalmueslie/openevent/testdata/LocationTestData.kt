package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.LocationCrudService
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest

object LocationTestData {

    fun createRequest(
        street: String = "street",
        streetNumber: String = "nr",
        zip: String = "zip",
        city: String = "city",
        country: String = "country",
        additionalInfo: String = "info",
        lat: Double = 1.0,
        lon: Double = 2.0,
        size: Int = 3,
    ) = LocationChangeRequest(street, streetNumber, zip, city, country, additionalInfo, lat, lon, size)

    fun updateRequest(
        street: String = "street-update",
        streetNumber: String = "nr-update",
        zip: String = "zip-update",
        city: String = "city-update",
        country: String = "country-update",
        additionalInfo: String = "info-update",
        lat: Double = 10.0,
        lon: Double = 20.0,
        size: Int = 30,
    ) = LocationChangeRequest(street, streetNumber, zip, city, country, additionalInfo, lat, lon, size)

    fun create(
        service: LocationCrudService,
        actor: Account,
        event: Event,
        request: LocationChangeRequest = createRequest(),
    ): Location = service.create(actor, event, request)

}
