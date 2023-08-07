package de.sambalmueslie.openevent.infrastructure.geo


import de.sambalmueslie.openevent.core.model.Location
import de.sambalmueslie.openevent.core.model.LocationChangeRequest
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class GeoLocationService(private val client: GeoLocationClient) : GeoLocationResolver {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(GeoLocationService::class.java)
    }

    override fun get(location: Location): GeoLocation? {
        return get(location.country, location.zip, location.street, location.streetNumber, location.city)
    }

    override fun get(request: LocationChangeRequest): GeoLocation? {
        return get(request.country, request.zip, request.street, request.streetNumber, request.city)
    }

    private fun get(country: String, zip: String, street: String, streetNumber: String, city: String): GeoLocation? {
        try {
            val result = client.search(country, zip, "$streetNumber $street", city).firstOrNull() ?: return null
            return GeoLocation(result.lat.toDoubleOrNull() ?: 0.0, result.lon.toDoubleOrNull() ?: 0.0)
        } catch (e: Exception) {
            logger.error("Cannot resolve geo location", e)
        }
        return null
    }


}
