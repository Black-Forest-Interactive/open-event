package de.sambalmueslie.openevent.gateway.app.account

import de.sambalmueslie.openevent.core.account.api.*
import io.micronaut.http.annotation.*
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/account")
@Tag(name = "APP Account API")
class AccountController(private val service: AccountGuardService) {

    @Get()
    fun get(auth: Authentication) = service.get(auth)

    @Put()
    fun update(auth: Authentication, @Body request: AccountChangeRequest) = service.update(auth, request)

    @Get("/validate")
    fun validate(auth: Authentication, @QueryValue lang: String) = service.validate(auth, lang)

    @Get("/profile")
    fun getProfile(auth: Authentication) = service.getProfile(auth)

    @Put("/profile")
    fun updateProfile(auth: Authentication, @Body request: ProfileChangeRequest) = service.updateProfile(auth, request)

    @Get("/preferences")
    fun getPreferences(auth: Authentication) = service.getPreferences(auth)

    @Put("/preferences")
    fun updatePreferences(auth: Authentication, @Body request: PreferencesChangeRequest) = service.updatePreferences(auth, request)
}
