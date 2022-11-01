package de.sambalmueslie.openevent.location.db

import de.sambalmueslie.openevent.common.crud.DataObject
import de.sambalmueslie.openevent.location.api.GeoLocation
import de.sambalmueslie.openevent.location.api.GeoLocationChangeRequest
import jakarta.persistence.*

@Suppress("JpaObjectClassSignatureInspection")
@Entity(name = "GeoLocation")
@Table(name = "geo_location")
data class GeoLocationData(
    @Id @GeneratedValue(strategy = GenerationType.SEQUENCE)
    var id: Long,
    @Column()
    var locationId: Long,
    @Column()
    var lat: Double,
    @Column()
    var lon: Double
) : DataObject<GeoLocation> {

    companion object {
        fun create(locationId: Long, request: GeoLocationChangeRequest) = GeoLocationData(0, locationId, request.lat, request.lon)
        val DEFAULT = GeoLocationData(0, 0, 0.0, 0.0)
    }

    override fun convert() = GeoLocation(id, lat, lon)

    fun update(request: GeoLocationChangeRequest): GeoLocationData {
        lat = request.lat
        lon = request.lon
        return this
    }
}
