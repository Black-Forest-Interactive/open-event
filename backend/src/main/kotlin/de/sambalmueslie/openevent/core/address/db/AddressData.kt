package de.sambalmueslie.openevent.core.address.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Address")
@Table(name = "address")
data class AddressData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,
    @Column var street: String,
    @Column var streetNumber: String,
    @Column var zip: String,
    @Column var city: String,
    @Column var country: String,
    @Column var additionalInfo: String,

    @Column var lat: Double,
    @Column var lon: Double,

    @Column var accountId: Long,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Address> {

    companion object {
        fun create(
            account: Account,
            request: AddressChangeRequest,
            timestamp: LocalDateTime
        ): AddressData {
            return AddressData(
                0,
                request.street,
                request.streetNumber,
                request.zip,
                request.city,
                request.country,
                request.additionalInfo,
                request.lat,
                request.lon,
                account.id,
                timestamp
            )
        }
    }

    override fun convert(): Address {
        return Address(id, street, streetNumber, zip, city, country, additionalInfo, lat, lon)
    }

    fun update(request: AddressChangeRequest, timestamp: LocalDateTime): AddressData {
        street = request.street
        streetNumber = request.streetNumber
        zip = request.zip
        city = request.city
        country = request.country
        additionalInfo = request.additionalInfo
        lat = request.lat
        lon = request.lon
        updated = timestamp
        return this
    }
}
