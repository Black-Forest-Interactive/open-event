package de.sambalmueslie.openevent.gateway.backoffice.account

import de.sambalmueslie.openevent.core.account.api.*
import de.sambalmueslie.openevent.core.address.api.AddressChangeRequest
import de.sambalmueslie.openevent.core.event.api.EventChangeRequest
import de.sambalmueslie.openevent.core.search.api.AccountSearchRequest
import de.sambalmueslie.openevent.core.search.api.AccountSearchResponse
import io.micronaut.data.model.Page
import io.micronaut.data.model.Pageable
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/backoffice/account")
@Tag(name = "BACKOFFICE Account API")
class AccountController(private val service: AccountGuardService) {

    @Get("/validate")
    fun validate(auth: Authentication, @QueryValue lang: String) = service.validate(auth, lang)

    @Get("/{id}")
    fun get(auth: Authentication, id: Long) = service.get(auth, id)

    @Get("/{id}/profile")
    fun getProfile(auth: Authentication, id: Long) = service.getProfile(auth, id)

    @Get("/{id}/preferences")
    fun getPreferences(auth: Authentication, id: Long) = service.getPreferences(auth, id)

    @Get("/{id}/address")
    fun getAddress(auth: Authentication, id: Long, pageable: Pageable) = service.getAddress(auth, id, pageable)

    @Post("/{id}/address")
    fun createAddress(auth: Authentication, id: Long, @Body request: AddressChangeRequest) = service.createAddress(auth, id, request)

    @Post("/{id}/address/import")
    fun importLocations(auth: Authentication, id: Long) = service.importLocations(auth, id)

    @Get("/{id}/event")
    fun getEvent(auth: Authentication, id: Long, pageable: Pageable) = service.getEvent(auth, id, pageable)

    @Post("/{id}/event")
    fun createEvent(auth: Authentication, id: Long, @Body request: EventChangeRequest) = service.createEvent(auth, id, request)

    @Post("/search")
    fun search(auth: Authentication, @Body request: AccountSearchRequest, pageable: Pageable): AccountSearchResponse = service.search(auth, request, pageable)

    @Post()
    fun create(auth: Authentication, @Body request: AccountChangeRequest) = service.create(auth, request)

    @Put("/{id}")
    fun update(auth: Authentication, id: Long, @Body request: AccountChangeRequest) = service.update(auth, id, request)

    @Delete("/{id}")
    fun delete(auth: Authentication, id: Long) = service.delete(auth, id)

    @Post("/setup")
    fun setup(auth: Authentication, @Body request: AccountSetupRequest) = service.setup(auth, request)

    @Put("/setup/{id}")
    fun updateSetup(auth: Authentication, id: Long, @Body request: AccountSetupRequest) = service.updateSetup(auth, id, request)
}
