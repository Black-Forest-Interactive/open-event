package de.sambalmueslie.openevent.location


import de.sambalmueslie.openevent.common.auth.checkPermission
import de.sambalmueslie.openevent.location.api.Location
import de.sambalmueslie.openevent.location.api.LocationAPI
import de.sambalmueslie.openevent.location.api.LocationChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.Controller
import io.micronaut.security.authentication.Authentication
import reactor.core.publisher.Mono

@Controller("/api/location")
class LocationController(private val service: LocationService) : LocationAPI {

    override fun get(auth: Authentication, id: Long): Mono<Location> = auth.checkPermission(LocationAPI.PERMISSION_LOCATION_READ) {
        Mono.justOrEmpty(service.get(id))
    }

    override fun getAll(auth: Authentication, pageable: Pageable): Mono<Page<Location>> = auth.checkPermission(LocationAPI.PERMISSION_LOCATION_READ) {
        Mono.just(service.getAll(pageable))
    }

    override fun create(auth: Authentication, request: LocationChangeRequest): Mono<Location> = auth.checkPermission(LocationAPI.PERMISSION_LOCATION_WRITE) {
        Mono.just(service.create(request))
    }

    override fun update(auth: Authentication, id: Long, request: LocationChangeRequest): Mono<Location> = auth.checkPermission(LocationAPI.PERMISSION_LOCATION_WRITE) {
        Mono.just(service.update(id, request))
    }

    override fun delete(auth: Authentication, id: Long): Mono<Location> = auth.checkPermission(LocationAPI.PERMISSION_LOCATION_WRITE) {
        Mono.justOrEmpty(service.delete(id))
    }

}
