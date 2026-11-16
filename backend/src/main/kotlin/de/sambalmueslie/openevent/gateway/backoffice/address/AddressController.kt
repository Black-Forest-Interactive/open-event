package de.sambalmueslie.openevent.gateway.backoffice.address

import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/address")
@Tag(name = "BACKOFFICE Address API")
class AddressController(private val service: AddressGuardService) {

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get()
    fun getAll(auth: Authentication, pageable: Pageable) = service.getAll(auth, pageable)

    @Post()
    fun create(auth: Authentication, @Body request: AddressChangeRequest) = service.create(auth, request)

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AddressChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long) = service.delete(auth, id)

    @Post("/import")
    fun importLocations(auth: Authentication) = service.importLocations(auth)
}
