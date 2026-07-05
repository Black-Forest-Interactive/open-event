package de.sambalmueslie.openevent.gateway.app.settings

import de.sambalmueslie.openevent.infrastructure.settings.SettingsService
import de.sambalmueslie.openevent.infrastructure.settings.api.BooleanResponse
import de.sambalmueslie.openevent.infrastructure.settings.api.TextResponse
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.authentication.Authentication
import io.swagger.v3.oas.annotations.tags.Tag

@Controller("/api/app/settings")
@Tag(name = "APP Settings API")
class SettingsController(private val service: SettingsService) {

    @Get("title")
    fun getTitle(auth: Authentication) = TextResponse(service.getTitle())

    @Get("terms")
    fun getTerms(auth: Authentication) = TextResponse(service.getTerms())

    @Get("registration-validate-code")
    fun getValidateRegistrationCode(auth: Authentication) = BooleanResponse(service.getValidateRegistrationCode())
}
