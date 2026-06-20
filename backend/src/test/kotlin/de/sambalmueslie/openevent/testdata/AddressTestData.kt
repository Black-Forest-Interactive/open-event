package de.sambalmueslie.openevent.testdata

import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.address.AddressCrudService
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest

object AddressTestData {

    fun createRequest(
        street: String = "street",
        streetNumber: String = "nr",
        zip: String = "zip",
        city: String = "city",
        country: String = "country",
        additionalInfo: String = "info",
        lat: Double = 1.0,
        lon: Double = 2.0,
    ) = AddressChangeRequest(street, streetNumber, zip, city, country, additionalInfo, lat, lon)

    fun updateRequest(
        street: String = "street-update",
        streetNumber: String = "nr-update",
        zip: String = "zip-update",
        city: String = "city-update",
        country: String = "country-update",
        additionalInfo: String = "info-update",
        lat: Double = 10.0,
        lon: Double = 20.0,
    ) = AddressChangeRequest(street, streetNumber, zip, city, country, additionalInfo, lat, lon)

    fun create(
        service: AddressCrudService,
        actor: Account,
        account: Account,
        request: AddressChangeRequest = createRequest(),
    ): Address = service.create(actor, account, request)

}
