package de.sambalmueslie.openevent.core.api


import de.sambalmueslie.openevent.api.LocationAPI
import de.sambalmueslie.openevent.api.LocationAPI.Companion.PERMISSION_READ
import de.sambalmueslie.openevent.api.LocationAPI.Companion.PERMISSION_WRITE
import de.sambalmueslie.openevent.core.auth.checkPermission
import de.sambalmueslie.openevent.core.logic.location.LocationCrudService
import de.sambalmueslie.openevent.core.model.Location
import de.sambalmueslie.openevent.core.model.LocationChangeRequest
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/location")
@Tag(name = "Location API")
class LocationController(private val service: LocationCrudService) : LocationAPI {

    @Get("/{id}")
    override fun get(auth: Authentication, id: Long): Location? {
        return auth.checkPermission(PERMISSION_READ) { service.get(id) }
    }

    @Get()
    override fun getAll(auth: Authentication, pageable: Pageable): Page<Location> {
        return auth.checkPermission(PERMISSION_READ) { service.getAll(pageable) }
    }

    @Post()
    override fun create(auth: Authentication, @Body request: LocationChangeRequest): Location {
        return auth.checkPermission(PERMISSION_WRITE) { service.create(request) }
    }

    @Put("/{id}")
    override fun update(auth: Authentication, id: Long, @Body request: LocationChangeRequest): Location {
        return auth.checkPermission(PERMISSION_WRITE) { service.update(id, request) }
    }

    @Delete("/{id}")
    override fun delete(auth: Authentication, id: Long): Location? {
        return auth.checkPermission(PERMISSION_WRITE) { service.delete(id) }
    }

}
