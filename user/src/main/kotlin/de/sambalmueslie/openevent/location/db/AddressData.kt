package de.sambalmueslie.openevent.location.db

import de.sambalmueslie.openevent.common.crud.DataObject
import de.sambalmueslie.openevent.location.api.Address
import de.sambalmueslie.openevent.location.api.AddressChangeRequest
import jakarta.persistence.*

@Suppress("JpaObjectClassSignatureInspection")
@Entity(name = "Address")
@Table(name = "address")
data class AddressData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    @Column()
    var locationId: Long,
    @Column()
    var street: String,
    @Column()
    var streetNumber: String,
    @Column()
    var zip: String,
    @Column()
    var city: String,
    @Column()
    var country: String,
    @Column()
    var additionalInfo: String
) : DataObject<Address> {
    companion object {
        fun create(locationId: Long, request: AddressChangeRequest) =
            AddressData(0, locationId, request.street, request.streetNumber, request.zip, request.city, request.country, request.additionalInfo)

        val DEFAULT = AddressData(0, 0, "", "", "", "", "", "")
    }

    override fun convert() = Address(id, street, streetNumber, zip, city, country, additionalInfo)

    fun update(request: AddressChangeRequest): AddressData {
        street = request.street
        streetNumber = request.streetNumber
        zip = request.zip
        city = request.city
        country = request.country
        additionalInfo = request.additionalInfo
        return this
    }

}
