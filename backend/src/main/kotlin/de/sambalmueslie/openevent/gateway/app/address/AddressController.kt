package de.sambalmueslie.openevent.gateway.app.address

import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/address")
@Tag(name = "APP Address API")
class AddressController(private val service: AddressGuardService) {

    @Get()
    fun get(auth: Authentication, pageable: Pageable) = service.get(auth, pageable)

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Post("/import")
    fun importLocations(auth: Authentication) = service.importLocations(auth)

    @Post()
    fun create(auth: Authentication, @Body request: AddressChangeRequest) = service.create(auth, request)

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AddressChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long) = service.delete(auth, id)

    @Put("/{id}/default")
    fun setDefault(auth: Authentication, id: Long) = service.setDefault(auth, id)
}
