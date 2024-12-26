package de.sambalmueslie.openevent.core.address


import de.sambalmueslie.openevent.common.BaseCrudService
import de.sambalmueslie.openevent.common.PageableSequence
import de.sambalmueslie.openevent.core.account.api.Account
import de.sambalmueslie.openevent.core.address.api.Address
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.address.db.AddressStorage
import de.sambalmueslie.openevent.core.event.EventCrudService
import de.sambalmueslie.openevent.core.location.LocationCrudService
import de.sambalmueslie.openevent.infrastructure.geo.GeoLocationResolver
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import jakarta.inject.Singleton
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Singleton
class AddressCrudService(
    private val storage: AddressStorage,
    private val geoAddressResolver: GeoLocationResolver,
    private val eventService: EventCrudService,
    private val locationService: LocationCrudService,
) : BaseCrudService<Long, Address, AddressChangeRequest, AddressChangeListener>(storage) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AddressCrudService::class.java)
    }

    fun create(actor: Account, account: Account, request: AddressChangeRequest): Address {
        val result = storage.create(resolveGeoAddress(request), account)
        notifyCreated(actor, result)
        return result
    }

    private fun resolveGeoAddress(request: AddressChangeRequest): AddressChangeRequest {
        val geoAddress = geoAddressResolver.get(request) ?: return request

        return AddressChangeRequest(
            request.street,
            request.streetNumber,
            request.zip,
            request.city,
            request.country,
            request.additionalInfo,
            geoAddress.lat,
            geoAddress.lon
        )
    }

    fun getAllForAccount(account: Account, pageable: Pageable): Page<Address> {
        return storage.findByAccount(account, pageable)
    }

    fun importLocations(account: Account): Page<Address> {

        PageableSequence { eventService.getOwned(account, it) }
            .mapNotNull {
                val l = locationService.findByEvent(it) ?: return@mapNotNull null
                AddressChangeRequest(
                    l.street,
                    l.streetNumber,
                    l.zip,
                    l.city,
                    l.country,
                    l.additionalInfo,
                    l.lat,
                    l.lon
                )
            }
            .distinctBy { "${it.street.trim()}${it.streetNumber.trim()}${it.zip.trim()}${it.city.trim()}${it.country.trim()}${it.additionalInfo.trim()}" }
            .forEach { create(account, account, it) }

        return getAllForAccount(account, Pageable.from(0, 20))
    }


}
