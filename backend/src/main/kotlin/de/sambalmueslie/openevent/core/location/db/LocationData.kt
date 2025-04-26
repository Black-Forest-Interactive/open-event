package de.sambalmueslie.openevent.core.location.db

import de.sambalmueslie.openevent.common.SimpleDataObject
import de.sambalmueslie.openevent.core.event.api.Event
import de.sambalmueslie.openevent.core.location.api.Location
import de.sambalmueslie.openevent.core.location.api.LocationChangeRequest
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity(name = "Location")
@Table(name = "location")
data class LocationData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE) var id: Long = 0,

    @Column var eventId: Long,

    @Column var street: String,
    @Column var streetNumber: String,
    @Column var zip: String,
    @Column var city: String,
    @Column var country: String,
    @Column var additionalInfo: String,

    @Column var lat: Double,
    @Column var lon: Double,

    @Column var size: Int,

    @Column var created: LocalDateTime = LocalDateTime.now(),
    @Column var updated: LocalDateTime? = null
) : SimpleDataObject<Location> {
    companion object {
        fun create(
            event: Event,
            request: LocationChangeRequest,
            timestamp: LocalDateTime
        ): LocationData {
            return LocationData(
                0,
                event.id,
                request.street,
                request.streetNumber,
                request.zip,
                request.city,
                request.country,
                request.additionalInfo,
                request.lat,
                request.lon,
                request.size,
                timestamp
            )
        }
    }

    override fun convert(): Location {
        return Location(id, eventId, street, streetNumber, zip, city, country, additionalInfo, lat, lon, size)
    }

    fun update(request: LocationChangeRequest, timestamp: LocalDateTime): LocationData {
        street = request.street
        streetNumber = request.streetNumber
        zip = request.zip
        city = request.city
        country = request.country
        additionalInfo = request.additionalInfo
        lat = request.lat
        lon = request.lon
        size = request.size
        updated = timestamp
        return this
    }
}

