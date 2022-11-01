package de.sambalmueslie.openevent.location


import de.sambalmueslie.openevent.common.crud.RelationalCrudService
import de.sambalmueslie.openevent.common.error.InvalidRequestException
import de.sambalmueslie.openevent.common.time.TimeProvider
import de.sambalmueslie.openevent.location.api.AddressChangeRequest
import de.sambalmueslie.openevent.location.api.GeoLocationChangeRequest
import de.sambalmueslie.openevent.location.api.Location
import de.sambalmueslie.openevent.location.api.LocationChangeRequest
import de.sambalmueslie.openevent.location.db.*
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class LocationService(
    repository: LocationRepository,
    private val addressRepository: AddressRepository,
    private val geoLocationRepository: GeoLocationRepository,
    private val timeProvider: TimeProvider
) : RelationalCrudService<Long, Location, LocationChangeRequest, LocationData>(repository, logger) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(LocationService::class.java)
    }

    override fun createData(request: LocationChangeRequest) = LocationData.create(request, timeProvider.now())
    override fun createRelationalData(request: LocationChangeRequest, data: LocationData) {
        addressRepository.save(AddressData.create(data.id, request.address))
        geoLocationRepository.save(GeoLocationData.create(data.id, request.geoLocation))
    }

    override fun updateData(data: LocationData, request: LocationChangeRequest) = data.update(request, timeProvider.now())

    override fun updateRelationalData(request: LocationChangeRequest, data: LocationData) {
        val address = addressRepository.findByLocationId(data.id)
        if (address != null) {
            addressRepository.update(address.update(request.address))
        } else {
            addressRepository.save(AddressData.create(data.id, request.address))
        }

        val geoLocation = geoLocationRepository.findByLocationId(data.id)
        if (geoLocation != null) {
            geoLocationRepository.update(geoLocation.update(request.geoLocation))
        } else {
            geoLocationRepository.save(GeoLocationData.create(data.id, request.geoLocation))
        }
    }

    override fun deleteRelationalData(data: LocationData) {
        addressRepository.deleteByLocationId(data.id)
        geoLocationRepository.deleteByLocationId(data.id)
    }

    override fun convert(data: LocationData): Location {
        val address = (addressRepository.findByLocationId(data.id) ?: AddressData.DEFAULT).convert()
        val geoLocation = (geoLocationRepository.findByLocationId(data.id) ?: GeoLocationData.DEFAULT).convert()
        return Location(data.id, address, geoLocation, data.capacity)
    }

    override fun isValid(request: LocationChangeRequest) {
        if (request.capacity < 0) throw InvalidRequestException("Capacity must be a positive value")
        isValid(request.address)
        isValid(request.geoLocation)
    }

    private fun isValid(request: AddressChangeRequest) {
        if (request.street.isBlank()) throw InvalidRequestException("Address must have an street")
        if (request.streetNumber.isBlank()) throw InvalidRequestException("Address must have an street number")
        if (request.zip.isBlank()) throw InvalidRequestException("Address must have an zip")
        if (request.city.isBlank()) throw InvalidRequestException("Address must have an city")
        if (request.country.isBlank()) throw InvalidRequestException("Address must have an country")
    }

    private fun isValid(request: GeoLocationChangeRequest) {
        // intentionally left empty
    }

}
